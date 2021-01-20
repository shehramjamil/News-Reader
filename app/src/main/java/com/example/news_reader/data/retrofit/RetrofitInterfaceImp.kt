package com.example.news_reader.data.retrofit


import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.domain.retrofit.RetrofitInterface
import retrofit2.Response
import retrofit2.http.*

// All the API end points can be written in this file.

interface RetrofitInterfaceIml: RetrofitInterface {

    @GET("v2/top-headlines/")
    override suspend fun getNews(
        @Query("country") countryName: String,
        @Query("pageSize") pageSize: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

}