package com.example.news_reader.data.mappers

import com.example.news_reader.data.model.json.ArticlesItem
import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.data.model.room.News
import javax.inject.Inject

class NewsMapper @Inject constructor(){

    fun networkToLocalModelMapping(newsResponse: NewsResponse?):ArrayList<News>
    {
        val newsArrayList: ArrayList<News> = ArrayList()
        newsResponse?.articles?.forEach {
             newsArrayList.add(
                News(
                    title = it.title,
                    publishedDataTime = it.publishedAt,
                    newsDescription = it.description,
                    imageURL = it.urlToImage
                )
            )
        }
        return newsArrayList
    }
}