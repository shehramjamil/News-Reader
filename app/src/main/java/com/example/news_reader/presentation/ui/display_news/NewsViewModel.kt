package com.example.news_reader.presentation.ui.display_news

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.presentation.ui.NewsViewModelInterface
import com.example.news_reader.utils.NetworkResponse
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
class NewsViewModel @ViewModelInject constructor(var newsRepository: NewsRepository) : ViewModel(),
    NewsViewModelInterface {

    init {
        // Initialisation Block
    }
    val fetchNews : LiveData<NetworkResponse<NewsResponse>>
        get() {
            return newsRepository.newsDataFromServer
        }


}