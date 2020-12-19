package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.*
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.data.work_manager.NewsWorkManager
import com.example.news_reader.utils.NetworkResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.concurrent.TimeUnit


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    var newsRepository: NewsRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    init {
        setNewsWorkManager(context)
    }


    val newsData: LiveData<NetworkResponse<List<NewsBuisnessModel>>>
        get() = newsRepository.getNewsDataLocally().asLiveData()


    private fun setNewsWorkManager(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val newsDownloadWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<NewsWorkManager>(4, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag("News-Downloader")
                // .setInputData(workDataOf("Signal" to "Some Data"))
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                "newsDownloader",
                ExistingPeriodicWorkPolicy.KEEP,
                newsDownloadWorkRequest
            )

    }


}