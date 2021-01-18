package com.example.news_reader.domain.repositories

import com.example.news_reader.data.model.room.News
import kotlinx.coroutines.flow.Flow

interface NewsRepositoryInterface {
    fun getNewsDataFromRoom() : Flow<List<News>>?
    suspend fun getNewsDataFromServerAndSaveInRoom() : Int
}