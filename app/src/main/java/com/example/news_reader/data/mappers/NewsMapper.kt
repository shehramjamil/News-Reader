package com.example.news_reader.data.mappers

import com.example.news_reader.domain.models.NewsBusinessModel
import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.data.model.room.News
import javax.inject.Inject

class NewsMapper @Inject constructor() {

    fun networkToLocalModelMapping(newsResponse: NewsResponse?, countryName: String): List<News>? {
        return newsResponse?.articles?.map {
            News(
                title = it.title,
                publishedDataTime = it.publishedAt,
                newsDescription = it.description,
                imageURL = it.urlToImage,
                webURL = it.url,
                countryName = countryName
            )
        }

    }

    fun localToBuisnessModelMapping(newsList: List<News>): List<NewsBusinessModel> {
        return newsList.map {
            NewsBusinessModel(
                title = it.title,
                publishedDataTime = it.publishedDataTime,
                imageURL = it.imageURL,
                newsDescription = it.newsDescription,
                webURL = it.webURL,
                countryName = it.countryName
            )
        }
    }

}