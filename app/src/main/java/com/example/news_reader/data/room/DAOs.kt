package com.example.news_reader.data.room

import androidx.room.*
import com.example.news_reader.data.model.room.News
import kotlinx.coroutines.flow.Flow

// This Class is responsible for declaring all the DAO interfaces

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(news: ArrayList<News>)

    @Query("SELECT * FROM newsData")
    fun getAll(): Flow<List<News>>

    @Query("SELECT COUNT(*)  FROM newsData")
    fun checkIfEmpty():Flow<Int>

    @Delete
    fun delete(news: News)

}
/*@Query("SELECT * FROM user WHERE uid IN (:userIds)")
  fun loadAllByIds(userIds: IntArray): List<User>

  @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
          "last_name LIKE :last LIMIT 1")
  fun findByName(first: String, last: String): User
  */