package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.news_reader.domain.usecases.GetRemoteDataAndSaveInRoomUseCase
import com.example.news_reader.domain.usecases.NewsDataFromRoomUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val newsDataFromRoomUseCase: NewsDataFromRoomUseCase,
    private val getRemoteDataAndSaveInRoomUseCase: GetRemoteDataAndSaveInRoomUseCase
) : ViewModel() {

    fun getNewsDataFromRoom(position: Int) = newsDataFromRoomUseCase.invoke(position)?.asLiveData()

    val serverResponse: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getSpecificCountryNewsDataFromServer(countryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getRemoteDataAndSaveInRoomUseCase.invoke(countryName, pageSize, apiKey)
            serverResponse.postValue(response)
        }
    }

    companion object QueryParameters {
        const val pageSize: String = "50"
        const val apiKey: String = "c39c26bf69914fb2bc927a8956baea5e"
    }


}