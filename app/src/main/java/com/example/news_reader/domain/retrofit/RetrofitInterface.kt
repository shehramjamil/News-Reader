package com.example.news_reader.domain.retrofit

import com.example.news_reader.data.model.json.NewsResponse
import retrofit2.Response
import retrofit2.http.Query

interface RetrofitInterface {
    suspend fun getNews(
        country: String,
        pageSize: String,
        page: String,
        category:String,
        apikey: String
    ): Response<NewsResponse>
}