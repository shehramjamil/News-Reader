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
    fun invoke(position: Int) = newsRepository.getNewsDataFromRoom(position)?.transform {
        if (it.isNullOrEmpty()) {
            emit(CustomResponseHandler.loading(null))
        } else {
            emit(CustomResponseHandler.success(newsMapper.localToBuisnessModelMapping(it)))
        }
    }

}