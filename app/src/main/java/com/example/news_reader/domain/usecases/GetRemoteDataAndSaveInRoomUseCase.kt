package com.example.news_reader.domain.usecases

import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.utils.*
import javax.inject.Inject

// UseCase can be usable in the future in other viewModel and modifiable with little ease.

class GetRemoteDataAndSaveInRoomUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
) {

    suspend operator fun invoke(): String {
        return handleHTTPCodes(newsRepository.getNewsDataFromServerAndSaveInRoom())
    }

    private fun handleHTTPCodes(code: Int): String {
        return when (code) {
            200 -> CODE200
            400 -> CODE400
            401 -> CODE401
            429 -> CODE429
            500 -> CODE500
            else -> "Unknown Error"
        }

    }
}