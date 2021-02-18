package com.example.news_reader.hilt

import android.content.Context
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.NewsDaoImplementation
import com.example.news_reader.domain.room.NewsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkingModule {

    @Provides
    fun okHTTPClient(): OkHttpClient = OkHttpClient
        .Builder()
        .build()

    @Provides
    fun prepareRetrofit(): RetrofitInterfaceIml {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //only when using ReactiveX
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHTTPClient())  // We can add http interceptors later in okhttp
            .build()

        return retrofitBuilder.create(RetrofitInterfaceIml::class.java)
    }
}

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun prepareRoom(@ApplicationContext context: Context): RoomDB = RoomDB.invoke(context)

}
