package com.example.news_reader.data.mappers

import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.data.model.room.News
import javax.inject.Inject

class NewsMapper @Inject constructor() {

    fun networkToLocalModelMapping(newsResponse: NewsResponse?): List<News>? {
        return newsResponse?.articles?.map {
            News(
                title = it.title,
                publishedDataTime = it.publishedAt,
                newsDescription = it.description,
                imageURL = it.urlToImage
            )
        }

    }

    fun localToBuisnessModelMapping(newsList: News): NewsBuisnessModel {
        return NewsBuisnessModel(
                    title = newsList.title,
                    id = newsList.id,
                    publishedDataTime = newsList.publishedDataTime,
                    imageURL = newsList.imageURL,
                    newsDescription = newsList.newsDescription
                )

    }

    fun localToBuisnessModelMapping2(newsList: List<News>): List<NewsBuisnessModel> {
        return newsList.map {
            NewsBuisnessModel(
                title = it.title,
                id = it.id,
                publishedDataTime = it.publishedDataTime,
                imageURL = it.imageURL,
                newsDescription = it.newsDescription
            )
        }

    }

}