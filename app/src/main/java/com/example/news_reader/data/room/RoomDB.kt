package com.example.news_reader.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.news_reader.data.model.room.News


@Database(entities = [News::class], version = 1, exportSchema = false)
abstract class RoomDB : RoomDatabase() {

    // Create abstract methods for any DAOs
    abstract fun News(): NewsDaoImplementation

    // Room Initialisation
    companion object {
        @Volatile
        private var instance: RoomDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            RoomDB::class.java, "News.db"
        ).allowMainThreadQueries()
            .build()
    }
}
