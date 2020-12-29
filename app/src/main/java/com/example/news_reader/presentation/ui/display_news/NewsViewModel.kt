package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.repositories.NewsRemoteMediatorRepository
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    @ApplicationContext var context: Context,
    var db: RoomDB,
    var retrofitInterfaceIml: RetrofitInterfaceIml,
    var newsMapper: NewsMapper
) : ViewModel() {

    val newsDao = db.news()

    @ExperimentalPagingApi
    val pager = Pager(
        config = PagingConfig(pageSize = 5, initialLoadSize = 5),
        remoteMediator = NewsRemoteMediatorRepository(newsDao, retrofitInterfaceIml, newsMapper)
    ) {
        newsDao.getAll()

    }.flow


}