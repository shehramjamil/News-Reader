package com.example.news_reader.data.retrofit


import com.example.news_reader.data.model.json.NewsResponse
import retrofit2.Response
import retrofit2.http.*

// All the API end points can be written in this file.

interface RetrofitInterface{

    @GET("v2/top-headlines?country=us&pageSize=20&apiKey=c39c26bf69914fb2bc927a8956baea5e")
    suspend fun getNews(): Response<NewsResponse>

}