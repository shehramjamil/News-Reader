package com.example.news_reader.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.model.room.News
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.domain.room.NewsDao
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediatorRepository(
    var newsDao: NewsDao,
    var retrofitInterfaceIml: RetrofitInterfaceIml,
    var newsMapper: NewsMapper
) : RemoteMediator<Int, News>() {

    companion object {
        const val pageSize = "20"
        const val apiKey = "c39c26bf69914fb2bc927a8956baea5e"
        const val country = "us"
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): MediatorResult {

        return try {
            // Load the key here for quering the remote server
            var pageNumber = 0
            when (loadType) {
                LoadType.REFRESH -> pageNumber = 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    pageNumber++
                }
            }
            // Here we can make a network request with the next load key
            val remoteNewsData =
                retrofitInterfaceIml.getNews(country, pageNumber.toString(), pageSize, apiKey)


            // Here we can insert the network data into our local database
            if (remoteNewsData.isSuccessful && remoteNewsData.body() != null) {
                newsDao.insertAll(newsMapper.networkToLocalModelMapping(remoteNewsData.body()))
            }



            return MediatorResult.Success(false)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}