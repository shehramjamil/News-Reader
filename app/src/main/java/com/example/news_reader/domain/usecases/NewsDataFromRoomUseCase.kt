package com.example.news_reader.domain.usecases

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.utils.CustomResponseHandler
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class NewsDataFromRoomUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsMapper: NewsMapper,
) {
    fun invoke(position: Int?,countryName: String) = newsRepository.getNewsDataFromRoom(position,countryName)?.transform {
        if (it.isNullOrEmpty()) {
            emit(CustomResponseHandler.loading(null))
        } else {
            kotlinx.coroutines.delay(1000)
            emit(CustomResponseHandler.success(newsMapper.localToBuisnessModelMapping(it)))
        }
    }

}