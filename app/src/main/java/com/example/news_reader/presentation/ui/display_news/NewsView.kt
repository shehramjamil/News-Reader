package com.example.news_reader.presentation.ui.display_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.databinding.ActivityMainBinding
import com.example.news_reader.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NewsView : AppCompatActivity() {
    private val viewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var newsAdapter: NewsAdapter
    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        setupRecycler()
        lifecycleScope.launch {
            viewModel.newsData2.observe(this@NewsView, newsDataObserver())
        }


        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("newsDownloader")
            .observe(this) {
                if (it[0].state == WorkInfo.State.FAILED) {
                    val httpStatusInfo = handleHTTPCodes(it[0].outputData.getInt("Error Code",0))
                    Toast.makeText(this,httpStatusInfo, Toast.LENGTH_LONG).show()

                }
            }


    }

    private fun setupRecycler() {
        val recyclerView = bind.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this@NewsView)
        recyclerView.adapter = newsAdapter
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    private fun newsDataObserver(): Observer<CustomResponseHandler<List<NewsBuisnessModel>>> {
        return Observer {
            when (it.status) {
                CustomResponseHandler.Status.SUCCESS -> {
                    newsAdapter.addData(it.data!!)
                    progressBar.visibility = View.GONE
                }
                CustomResponseHandler.Status.ERROR -> {
                }
                CustomResponseHandler.Status.LOADING -> {
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


