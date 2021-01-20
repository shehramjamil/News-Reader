package com.example.news_reader.data.repositories

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.room.NewsDaoImplementation
import com.example.news_reader.domain.repositories.NewsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    db: RoomDB,
    private val retrofitInterfaceIml: RetrofitInterfaceIml,
    private val newsMapper: NewsMapper
) : NewsRepositoryInterface {

    private val newsDao: NewsDaoImplementation = db.News()

    override fun getNewsDataFromRoom(position: Int) =
        newsDao.getAll(position)
            ?.flowOn(Dispatchers.IO)
            ?.catch {
                //emit()
            }

    override suspend fun getNewsDataFromServerAndSaveInRoom(
        countryName: String,
        pageSize: String,
        apiKey: String
    ): Int {
        val response = retrofitInterfaceIml.getNews(countryName, pageSize, apiKey)
        return if (response.isSuccessful && response.body() != null) {
            newsDao.insertAll(newsMapper.networkToLocalModelMapping(response.body()))
            response.code()
        } else {
            response.code()
        }
    }


}
