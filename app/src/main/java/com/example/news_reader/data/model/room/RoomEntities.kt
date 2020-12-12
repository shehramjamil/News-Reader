package com.example.news_reader.data.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "newsData")
data class News(
    @PrimaryKey
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "publishedDateTime")
    var publishedDataTime: String,
    @ColumnInfo(name = "news_description") val newsDescription: String?,
    @ColumnInfo(name = "image_url") val imageURL: String?
)