package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.news_reader.domain.usecases.NewsDataFromRoomUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val NewsDataFromRoomUseCase: NewsDataFromRoomUseCase
) : ViewModel() {

    val newsData2
        get() = NewsDataFromRoomUseCase.invoke().asLiveData()


}