    package com.example.news_reader.data.room

import androidx.room.*
import com.example.news_reader.data.model.room.News
import com.example.news_reader.domain.room.NewsDao
import kotlinx.coroutines.flow.Flow

// This Class is responsible for declaring all the DAO interfaces

@Dao
interface NewsDaoImplementation : NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override fun insertAll(news: List<News>?)

    @Query("SELECT * FROM newsData WHERE countryName = :countryName order by publishedDateTime DESC LIMIT 15 OFFSET :position")
    override fun getAll(position: Int?, countryName: String): Flow<List<News>>?

    @Query("SELECT COUNT(*)  FROM newsData")
    override fun checkIfEmpty(): Flow<Int>

    @Query("DELETE FROM newsData")
    override fun delete()

}
