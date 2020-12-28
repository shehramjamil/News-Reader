package com.example.news_reader.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.news_reader.data.model.room.News
import org.w3c.dom.NameList

@OptIn(ExperimentalPagingApi::class)
class ExampleRemoteMediator(
) : RemoteMediator<Int, News>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): MediatorResult {

        // Here we load up the last key or the first key or null in case of first request
        val loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                lastItem.id
            }
        }

        // Here we can make a network request with the next load key


        // Here we can insert the network data into our local database


      return  MediatorResult.Success(false)
    }
}