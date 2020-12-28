package com.example.news_reader.domain.room

import androidx.paging.PagingSource
import com.example.news_reader.data.model.room.News
import com.example.news_reader.domain.models.NewsBuisnessModel
import kotlinx.coroutines.flow.Flow

interface NewsDao {
    fun insertAll(news: List<News>?)
    fun getAll(): PagingSource<Int,News>
    fun checkIfEmpty():Flow<Int>
    fun delete(news: News)
}