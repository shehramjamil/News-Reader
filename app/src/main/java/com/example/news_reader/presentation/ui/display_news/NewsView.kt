package com.example.news_reader.presentation.ui.display_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.paging.ExperimentalPagingApi
import androidx.paging.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.*
import com.example.news_reader.data.mappers.NewsMapper
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.databinding.ActivityMainBinding
import com.example.news_reader.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NewsView : AppCompatActivity() {
    private val viewModel by viewModels<NewsViewModel>()


    @Inject
    lateinit var newsAdapter: PagingAdapter2

    @Inject
    lateinit var newsMapper: NewsMapper

    private lateinit var bind: ActivityMainBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar


    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        swipeRefresh = bind.swipeRefresh
        progressBar = bind.progressBar

        setupRecycler()
        newsWorkRequestObserver()

        //viewModel.newsData.observe(this, newsLocalDataObserver())

        lifecycleScope.launch {

            viewModel.pager.collectLatest {
                    newsAdapter.submitData(it)

            /*it.map { news ->
                    Log.d("dataaaaaaaaaaaaaa", news.toString())
                   val list =  newsMapper.localToBuisnessModelMapping(news)
                    newsAdapter.addData(list)
                }*/
               }
        }


        swipeRefresh.setOnRefreshListener {
            viewModel.setNewsWorkManager()
        }


    }


    private fun setupRecycler() {
        val recyclerView = bind.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this@NewsView)
        recyclerView.adapter = newsAdapter
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    private fun newsWorkRequestObserver() {
        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("newsDownloader")
            .observe(this) { workInfoList ->
                swipeRefresh.isRefreshing = false
                when (workInfoList[0].state) {
                    WorkInfo.State.SUCCEEDED -> {
                        Toast.makeText(this, "Network Request Successful", Toast.LENGTH_LONG).show()
                    }

                    WorkInfo.State.FAILED -> {
                        val httpStatusInfo =
                            handleHTTPCodes(workInfoList[0].outputData.getInt("Error Code", 0))
                        Toast.makeText(this, httpStatusInfo, Toast.LENGTH_LONG).show()
                    }

                    else -> {
                    }
                }
            }
    }

    private fun newsLocalDataObserver(): Observer<CustomResponse<List<NewsBuisnessModel>>> {
        return Observer {
            when (it.status) {
                CustomResponse.Status.SUCCESS -> {

                    //newsAdapter.addData(it.data!!)
                    progressBar.visibility = View.GONE
                }
                CustomResponse.Status.ERROR -> {
                }
                CustomResponse.Status.LOADING -> {
                    Snackbar.make(bind.newsView, "Downloading News", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun handleHTTPCodes(code: Int): String {
        return when (code) {
            400 -> CODE400
            401 -> CODE401
            429 -> CODE429
            500 -> CODE500
            else -> "Unknown Error"
        }

    }

}


