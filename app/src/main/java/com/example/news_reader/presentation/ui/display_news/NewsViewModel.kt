package com.example.news_reader.presentation.ui.display_news

import android.content.Context
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import androidx.work.*
import com.example.news_reader.data.model.room.News
import com.example.news_reader.data.repositories.ExampleRemoteMediator
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.data.repositories.NewsRepository
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.data.work_manager.NewsWorkManager
import com.example.news_reader.utils.CustomResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.concurrent.TimeUnit


@ActivityScoped
class NewsViewModel @ViewModelInject constructor(
    var newsRepository: NewsRepository,
    @ApplicationContext var context: Context,
    db: RoomDB
) : ViewModel() {



    val newsDao = db.news()
    @ExperimentalPagingApi
    val pager = Pager(
        config = PagingConfig(pageSize = 20),
        remoteMediator = ExampleRemoteMediator()

    ) {
        newsDao.getAll()

    }.flow



    init {
        setNewsWorkManager()
    }

    val newsData: LiveData<CustomResponse<List<NewsBuisnessModel>>>
        get() = newsRepository.getNewsDataLocally().asLiveData()



    fun setNewsWorkManager() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val newsDownloadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<NewsWorkManager>()
                .setConstraints(constraints)
                .addTag("newsDownloader")
                .setInitialDelay(5, TimeUnit.SECONDS)
                // .setInputData(workDataOf("Signal" to "Some Data"))
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "newsDownloader",
                ExistingWorkPolicy.REPLACE,
                newsDownloadWorkRequest
            )

    }


}