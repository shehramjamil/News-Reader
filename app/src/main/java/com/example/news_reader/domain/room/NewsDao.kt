package com.example.news_reader.domain.room

import com.example.news_reader.data.model.room.News
import kotlinx.coroutines.flow.Flow

interface NewsDao {
    fun insertAll(news: List<News>?)
    fun getAll(position: Int?, countryName: String): Flow<List<News>>?
    fun checkIfEmpty(): Flow<Int>
    fun delete()
}