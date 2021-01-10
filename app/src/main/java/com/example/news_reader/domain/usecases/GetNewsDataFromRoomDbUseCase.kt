package com.example.news_reader.domain.usecases

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.utils.CustomResponseHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNewsDataFromRoomDbUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsMapper: NewsMapper
) {
    operator fun invoke() = flow {
        newsRepository.getNewsDataFromRoom()?.collect { newsList ->
            if (newsList.isNullOrEmpty()) {
                emit(CustomResponseHandler.loading(null))
            } else {
                emit(CustomResponseHandler.success(newsMapper.localToBuisnessModelMapping(newsList)))
            }
        }
    }

}