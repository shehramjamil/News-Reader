package com.example.news_reader.domain.retrofit

import com.example.news_reader.data.model.json.NewsResponse
import retrofit2.Response

interface RetrofitInterface {
    suspend fun getNews(
        countryName: String,
        pageSize: String,
        apiKey: String
    ): Response<NewsResponse>
}