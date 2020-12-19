package com.example.news_reader.hilt

import android.content.Context
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.retrofit.RetrofitInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class NetworkingModule {

    @Provides
    @Singleton
    fun okHTTPClient(): OkHttpClient = OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun prepareRetrofit(): RetrofitInterface {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //only when using ReactiveX
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHTTPClient())  // We can add http interceptors later in okhttp
            .build()

        return retrofitBuilder.create(RetrofitInterface::class.java)
    }
}

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule{
    @Provides
    @Singleton
    fun prepareRoom(@ApplicationContext context: Context): RoomDB = RoomDB.invoke(context)
}