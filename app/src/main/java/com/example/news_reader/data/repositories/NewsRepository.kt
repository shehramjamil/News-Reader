package com.example.news_reader.data.repositories

import com.example.news_reader.utils.CODE400
import com.example.news_reader.utils.CODE401
import com.example.news_reader.utils.CODE429
import com.example.news_reader.utils.CODE500
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterface
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.room.NewsDao
import com.example.news_reader.domain.repositories.NewsRepositoryInterface
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
) : NewsRepositoryInterface {

    private val newsDao: NewsDao = db.News()

    /*override fun getNewsDataLocally() = flow {
        if (cm.checkInternetAvailability()) {
            val newsDataFromServer = getNewsDataFromServer()
            if (newsDataFromServer != 200) {
                emit(NetworkResponse.error(handleHTTPCodes(newsDataFromServer)))
            }
        } else {
            emit(NetworkResponse.error("Internet Unavailable"))
        }
        newsDao.getAll()?.collect { newsList ->
            emit(NetworkResponse.success(newsMapper.localToBuisnessModelMapping(newsList)))
        }
    }*/


    override fun getNewsDataLocally() = flow {
        newsDao.getAll()?.collect { newsList ->
            if (newsList.isEmpty()) {
                emit(NetworkResponse.error("News Unavailable"))
            } else {
                emit(NetworkResponse.success(newsMapper.localToBuisnessModelMapping(newsList)))
            }
        }
    }


    override suspend fun getNewsDataFromServer(): Int {
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
