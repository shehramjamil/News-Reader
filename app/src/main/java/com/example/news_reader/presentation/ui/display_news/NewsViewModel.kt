package com.example.news_reader.presentation.ui.display_news

import androidx.lifecycle.*
import com.example.news_reader.domain.models.NewsBusinessModel
import com.example.news_reader.domain.usecases.GetRemoteDataAndSaveInRoomUseCase
import com.example.news_reader.domain.usecases.NewsDataFromRoomUseCase
import com.example.news_reader.utils.CustomResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsDataFromRoomUseCase: NewsDataFromRoomUseCase,
    private val getRemoteDataAndSaveInRoomUseCase: GetRemoteDataAndSaveInRoomUseCase
) : ViewModel() {

    var newsPagedDataFromRoom: LiveData<CustomResponseHandler<List<NewsBusinessModel>>>? =
        MutableLiveData()




    val serverResponse: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getSpecificCountryNewsDataFromRoom(scrollPosition: Int, countryName: String) {
        newsPagedDataFromRoom =
            newsDataFromRoomUseCase.invoke(scrollPosition, countryName)?.asLiveData()
    }

    fun getSpecificCountryNewsDataFromServer(countryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = getRemoteDataAndSaveInRoomUseCase.invoke(countryName, pageSize, apiKey)
            serverResponse.postValue(response)
        }
    }

    // On Insertion, room emit all data so filtration needed
    fun filterNewsData(
        countryKey: String,
        newsList: List<NewsBusinessModel>?
    ): List<NewsBusinessModel>? {
        return newsList?.filter {
            it.countryName == countryKey
        }
    }

    companion object QueryParameters {
        const val pageSize: String = "50"
        const val apiKey: String = "c39c26bf69914fb2bc927a8956baea5e"
    }
}

