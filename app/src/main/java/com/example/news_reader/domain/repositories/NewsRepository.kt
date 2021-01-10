package com.example.news_reader.domain.repositories

import com.example.news_reader.data.model.room.News
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.utils.CustomResponseHandler
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryInterface {
    fun getNewsDataFromRoom() : Flow<List<News>>?
}