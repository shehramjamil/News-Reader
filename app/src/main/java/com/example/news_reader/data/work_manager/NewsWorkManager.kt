package com.example.news_reader.data.work_manager

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.data.retrofit.RetrofitInterfaceIml
import com.example.news_reader.data.room.NewsDaoImpl
import com.example.news_reader.data.room.RoomDB
import com.example.news_reader.utils.CheckInternetAvailability

class NewsWorkManager @WorkerInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    var retrofitInterfaceIml: RetrofitInterfaceIml,
    var newsMapper: NewsMapper,
    db: RoomDB,
    var cm: CheckInternetAvailability
) :
    CoroutineWorker(appContext, workerParams) {

    private var newsDao: NewsDaoImpl = db.news()

    override suspend fun doWork(): Result {

        if (cm.checkInternetAvailability()) {
            val resultRetrofit = retrofitInterfaceIml.getNews()
            if (resultRetrofit.isSuccessful && resultRetrofit.body() != null) {
                newsDao.insertAll(newsMapper.networkToLocalModelMapping(resultRetrofit.body()))
                return Result.success()
            } else {

                return Result.failure(workDataOf("Error Code" to resultRetrofit.code()))
            }
        }
        return Result.failure()
    }
}