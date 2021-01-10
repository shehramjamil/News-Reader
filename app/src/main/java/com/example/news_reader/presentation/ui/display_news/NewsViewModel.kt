package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.*
import com.example.news_reader.data.work_manager.NewsWorkManager
import com.example.news_reader.domain.usecases.GetNewsDataFromRoomDbUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    @ApplicationContext context: Context,
    private val getNewsDataFromRoomDbUseCase: GetNewsDataFromRoomDbUseCase
) : ViewModel() {

    init {
        setNewsWorkManager(context)
    }

    val newsData2 get() = getNewsDataFromRoomDbUseCase.invoke().asLiveData()

    private fun setNewsWorkManager(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val newsDownloadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<NewsWorkManager>()
                .setConstraints(constraints)
                .addTag("newsDownloader")
                //.setInitialDelay(1,TimeUnit.MINUTES)
                // .setInputData(workDataOf("Signal" to "Some Data"))
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "newsDownloader",
                ExistingWorkPolicy.KEEP,
                newsDownloadWorkRequest
            )
    }


}