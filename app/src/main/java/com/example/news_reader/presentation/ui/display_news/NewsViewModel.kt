package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import androidx.room.withTransaction
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.repositories.NewsRemoteMediatorRepository
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.RoomDB
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.map


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    @ApplicationContext var context: Context,
    var db: RoomDB,
    var retrofitInterfaceIml: RetrofitInterfaceIml,
    var newsMapper: NewsMapper
) : ViewModel() {

    private val newsDao = db.news()

    @ExperimentalPagingApi
    val pager = Pager(
        config = PagingConfig(pageSize = 5, initialLoadSize = 10, enablePlaceholders = false),
        remoteMediator = NewsRemoteMediatorRepository(db, retrofitInterfaceIml, newsMapper)
    ) {
        newsDao.getAll()
    }.flow.cachedIn(viewModelScope).map {
        it.map {
            newsMapper.localToBuisnessModelMapping(it)
        }
    }


}