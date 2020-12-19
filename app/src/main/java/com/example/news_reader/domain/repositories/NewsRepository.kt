package com.example.news_reader.domain.repositories

import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.utils.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryInterface {
    fun getNewsDataLocally() : Flow<NetworkResponse<List<NewsBuisnessModel>>>
    suspend fun getNewsDataFromServer() :Int
}