package com.example.dagger_android.model


import com.example.news_reader.data.model.json.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

// All the API end points can be written in this file.

interface RetrofitInterface {


    @GET("v2/top-headlines?country=us&apiKey=c39c26bf69914fb2bc927a8956baea5e")
    fun getNews(): Call<NewsResponse>

}