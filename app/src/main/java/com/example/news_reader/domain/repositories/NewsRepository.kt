package com.example.news_reader.domain.repositories

import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.utils.CustomResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryInterface {
    fun getNewsDataLocally() : Flow<CustomResponse<List<NewsBuisnessModel>>>
}