package com.example.news_reader.data.repositories

import android.util.Log
import android.widget.Toast
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.model.room.News
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.domain.room.NewsDao
import com.example.news_reader.utils.CODE400
import com.example.news_reader.utils.CODE401
import com.example.news_reader.utils.CODE429
import com.example.news_reader.utils.CODE500
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediatorRepository(
    var db: RoomDB,
    var retrofitInterfaceIml: RetrofitInterfaceIml,
    var newsMapper: NewsMapper
) : RemoteMediator<Int, News>() {
    val newsDao = db.news()

    companion object QueryParameters {
        const val pageSize = "20"
        const val apiKey = "c39c26bf69914fb2bc927a8956baea5e"
        const val country = "us"
        const val category = "business"

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, News>
    ): MediatorResult {

        return try {

            // Load the page key or page Number here for querying the remote server
            // This News Api has not paging number so doing it manually
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = false)
                LoadType.APPEND -> 2
            }

            // Here we can make a network request with the next load key
            val remoteNewsData =
                retrofitInterfaceIml.getNews(
                    country,
                    pageSize,
                    loadKey.toString(),
                    category,
                    apiKey
                )

            // Here we can insert the network data into our local database
            val checkNewsIfEmpty = remoteNewsData.body()?.articles?.isEmpty()!!
            if (remoteNewsData.isSuccessful && !checkNewsIfEmpty) {
                db.withTransaction {
                    if(loadType == LoadType.REFRESH)
                    {
                        newsDao.delete()
                    }
                    newsDao.insertAll(newsMapper.networkToLocalModelMapping(remoteNewsData.body()))
                }
            }

            return MediatorResult.Success(endOfPaginationReached = remoteNewsData.body()?.articles == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun handleHTTPCodes(code: Int): String {
        return when (code) {
            400 -> CODE400
            401 -> CODE401
            429 -> CODE429
            500 -> CODE500
            else -> "Unknown Error"
        }

    }
}