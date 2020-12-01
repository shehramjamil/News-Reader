package com.example.news_reader.hilt

import com.example.dagger_android.model.RetrofitInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton




@InstallIn(ApplicationComponent::class)
@Module
class NetworkingModule {

    @Provides
    @Singleton
    fun okHTTPClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun prepareRetrofit(): RetrofitInterface {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHTTPClient())  // We can add http interceptors later
            .build()

        return retrofitBuilder.create(RetrofitInterface::class.java)
    }




}

