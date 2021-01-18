package com.example.news_reader.data.repositories

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.data.model.room.News
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.room.NewsDaoImplementation
import com.example.news_reader.domain.repositories.NewsRepositoryInterface
import com.example.news_reader.utils.CheckInternetAvailability
import com.example.news_reader.utils.CustomResponseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

// Repositories are good approach to get in touch with the remote and local database
// View Model will have less code and it supports the separation of concerns concept
// Implementing the single source of truth Concept To get data only from local database
// within a function, if there are two collectors with different scopes, the first one will run and the other one will be skipped.

@Singleton
class NewsRepository @Inject constructor(
    db: RoomDB,
    private val retrofitInterfaceIml: RetrofitInterfaceIml,
    private val newsMapper: NewsMapper
) : NewsRepositoryInterface {

    var coroutineContext = Dispatchers.IO

    private val newsDao: NewsDaoImplementation = db.News()

    override fun getNewsDataFromRoom() =
        newsDao.getAll()
            ?.flowOn(coroutineContext)
            ?.catch {
                //emit()
            }

    override suspend fun getNewsDataFromServerAndSaveInRoom(): Int {
        val response = retrofitInterfaceIml.getNews()
        return if (response.isSuccessful && response.body() != null) {
            newsDao.insertAll(newsMapper.networkToLocalModelMapping(response.body()))
            response.code()
        } else {
            response.code()
        }
    }


}
