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
            publishedDataTime = newsList.publishedDataTime,
            imageURL = newsList.imageURL,
            newsDescription = newsList.newsDescription
        )

    }


}

