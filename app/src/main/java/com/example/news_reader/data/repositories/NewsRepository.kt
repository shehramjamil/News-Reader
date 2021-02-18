package com.example.news_reader.data.repositories

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.room.NewsDaoImplementation
import com.example.news_reader.domain.repositories.NewsRepositoryInterface
import com.example.news_reader.utils.CheckInternetAvailability
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    db: RoomDB,
    private val retrofitInterfaceIml: RetrofitInterfaceIml,
    private val newsMapper: NewsMapper,
    private val checkInternetAvailability: CheckInternetAvailability
) : NewsRepositoryInterface {

    private val newsDao: NewsDaoImplementation = db.News()

    override fun getNewsDataFromRoom(position: Int?, countryName: String) =
        newsDao.getAll(position, countryName)
            ?.flowOn(Dispatchers.IO)
            ?.catch {
                emit(emptyList())
            }

    override suspend fun getNewsDataFromServerAndSaveInRoom(
        countryName: String,
        pageSize: String,
        apiKey: String
    ): Int {
        return if (checkInternetAvailability.checkInternetAvailability()) {
            val response = retrofitInterfaceIml.getNews(countryName, pageSize, apiKey)
            if (response.isSuccessful && response.body() != null) {
                //newsDao.delete()
                newsDao.insertAll(
                    newsMapper.networkToLocalModelMapping(
                        response.body(),
                        countryName
                    )
                )
                response.code()
            } else {
                response.code()
            }
        } else
            502
    }


}
