package com.example.news_reader.data.repositories

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.room.NewsDaoImplementation
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
    private val retrofitInterfaceIml: RetrofitInterfaceIml,
    db: RoomDB,
    private val cm: CheckInternetAvailability,
    private val newsMapper: NewsMapper
) : NewsRepositoryInterface {

    private val newsDao: NewsDaoImplementation = db.News()

    override fun getNewsDataLocally() = flow {
        newsDao.getAll()?.collect { newsList ->
            if (newsList.isEmpty()) {
                emit(NetworkResponse.loading(null))
            } else {
                emit(NetworkResponse.success(newsMapper.localToBuisnessModelMapping(newsList)))
            }
        }
    }



}
