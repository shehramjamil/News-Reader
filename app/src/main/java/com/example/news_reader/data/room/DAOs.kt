package com.example.news_reader.data.room

import androidx.paging.PagingSource
import androidx.room.*
import com.example.news_reader.data.model.room.News
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.domain.room.NewsDao
import kotlinx.coroutines.flow.Flow

// This Class is responsible for declaring all the DAO interfaces

@Dao
interface NewsDaoImpl : NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override fun insertAll(news: List<News>?)

    @Query("SELECT * FROM newsData")
    override  fun getAll(): PagingSource<Int,News>

    @Query("SELECT COUNT(*)  FROM newsData")
    override fun checkIfEmpty():Flow<Int>

    @Delete
    override fun delete(news: News)

}
