package com.example.news_reader.domain.usecases

import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.utils.CustomResponseHandler
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsDataFromRoomUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsMapper: NewsMapper,
    private val saveRemoteDataInRoomUseCase: GetRemoteDataAndSaveInRoomUseCase
) {
    operator fun invoke() = flow {
        val response = saveRemoteDataInRoomUseCase.invoke()
        if (response == "Successful") {
            newsRepository.getNewsDataFromRoom()?.map { newsList ->
                if (newsList.isNullOrEmpty()) {
                    emit(CustomResponseHandler.loading(null))
                } else {
                    emit(
                        CustomResponseHandler.success(
                            newsMapper.localToBuisnessModelMapping(
                                newsList
                            )
                        )
                    )
                }
            }
        } else {
            emit(CustomResponseHandler.error(message = response, data = null))
        }
    }
}