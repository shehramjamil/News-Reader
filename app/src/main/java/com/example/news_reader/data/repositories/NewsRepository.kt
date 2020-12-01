package com.example.news_reader.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dagger_android.model.RetrofitInterface
import com.example.news_reader.data.model.json.NewsResponse
import com.example.news_reader.data.model.room.RoomDatabase
import com.example.news_reader.utils.NetworkResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

// Repositories are good approach to get in touch with the remote and local database
// View Model will have less code and it supports the separation of concerns concept
// MutableLiveData class has two functions. setValue() method must be called from the main thread.
// But if you need set a value from a background thread, postValue() should be used.

@Singleton
class NewsRepository @Inject constructor(private val retrofitInterface: RetrofitInterface, private val roomDatabase: RoomDatabase) {


    val newsDataFromServer: LiveData<NetworkResponse<NewsResponse>>
        get() {
            val data = MutableLiveData<NetworkResponse<NewsResponse>>()
            retrofitInterface.getNews().enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful) {
                        data.postValue(NetworkResponse.success(response.body()))
                    } else {
                        // We can set conditions on the response codes and error body
                        data.postValue(NetworkResponse.error(response.toString()))
                    }

                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    data.postValue(NetworkResponse.error(t.message.toString()))
                }
            })
            return data
        }


}