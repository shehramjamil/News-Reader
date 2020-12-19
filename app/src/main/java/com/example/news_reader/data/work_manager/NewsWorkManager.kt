package com.example.news_reader.data.work_manager

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterface
import com.example.news_reader.data.room.NewsDao
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.utils.CheckInternetAvailability
import javax.inject.Inject
import kotlin.random.Random

class NewsWorkManager @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    var retrofitInterface: RetrofitInterface,
    var newsMapper: NewsMapper,
     db: RoomDB
) :
    CoroutineWorker(appContext, workerParams) {

    private var newsDao: NewsDao = db.News()

    override suspend fun doWork(): Result {

        val resultRetrofit = retrofitInterface.getNews()

        if (resultRetrofit.isSuccessful && resultRetrofit.body() != null) {
            newsDao.insertAll(newsMapper.networkToLocalModelMapping(resultRetrofit.body()))
            Log.d("Worker Message", resultRetrofit.message())
        }
        else
        {
            return Result.failure(workDataOf("Error Code" to resultRetrofit.code()))
        }
        return Result.success()
    }
}