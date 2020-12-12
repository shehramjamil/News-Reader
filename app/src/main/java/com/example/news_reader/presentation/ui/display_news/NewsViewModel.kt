package com.example.news_reader.presentation.ui.display_news

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.news_reader.data.model.room.News
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.utils.NetworkResponse
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(var newsRepository: NewsRepository) : ViewModel(){

    val newsData : LiveData<NetworkResponse<List<News>>> get() =  newsRepository.getNewsData().asLiveData()

}