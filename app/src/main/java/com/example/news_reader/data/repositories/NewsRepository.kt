package com.example.news_reader.data.repositories

import com.example.news_reader.CODE400
import com.example.news_reader.CODE401
import com.example.news_reader.CODE429
import com.example.news_reader.CODE500
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterface
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.model.room.News
import com.example.news_reader.data.room.NewsDao
import com.example.news_reader.utils.CheckInternetAvailability
import com.example.news_reader.utils.NetworkResponse
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// Repositories are good approach to get in touch with the remote and local database
// View Model will have less code and it supports the separation of concerns concept
// Implementing the single source of truth Concept To get data only from local database
// within a function, if there are two collectors with different scopes, the first one will run and the other one will be skipped.

@Singleton
class NewsRepository @Inject constructor(
    private val retrofitInterface: RetrofitInterface,
    db: RoomDB,
    private val cm: CheckInternetAvailability,
    private val newsMapper: NewsMapper
) {
    private val newsDao: NewsDao = db.News()


    fun getNewsData() = flow {
        if (cm.checkInternetAvailability()) {
            val newsDataFromServer = newsDataFromServer()
            if (newsDataFromServer != 200) {
                emit(NetworkResponse.error(handleHTTPCodes(newsDataFromServer)))
            } else {
                newsDao.getAll().collect { newsList ->
                    emit(NetworkResponse.success(newsList))
                }
            }
        } else {
            emit(NetworkResponse.error("Internet Unavailable"))
            newsDao.getAll().collect { newsList ->
                emit(NetworkResponse.success(newsList))
            }
        }
    }

    private suspend fun newsDataFromServer(): Int {
        val resultRetrofit = retrofitInterface.getNews()
        return if (resultRetrofit.isSuccessful) {
            newsDao.insertAll(newsMapper.networkToLocalModelMapping(resultRetrofit.body()))
            200
        } else {
            resultRetrofit.code()
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
