package com.example.news_reader.data.retrofit


import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.domain.retrofit.RetrofitInterface
import retrofit2.Response
import retrofit2.http.*

// All the API end points can be written in this file.

interface RetrofitInterfaceIml:RetrofitInterface{

    //@GET("v2/top-headlines?country=us&pageSize=20&apiKey=c39c26bf69914fb2bc927a8956baea5e")
    //override suspend fun getNews(): Response<NewsResponse>

    @GET("v2/top-headlines/")
    override suspend fun getNews(
        @Query("country") country: String,
        @Query("pageSize") pageSize: String,
        @Query("pageSize") page: String,
        @Query("apiKey") apikey: String
    ): Response<NewsResponse>

}