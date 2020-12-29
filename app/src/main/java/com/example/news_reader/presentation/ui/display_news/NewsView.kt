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
import java.lang.Exception
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

        lifecycleScope.launch {

            viewModel.pager.collectLatest {
                newsAdapter.submitData(it)
            }

            newsAdapter
                .withLoadStateHeaderAndFooter(
                    header = ExampleLoadStateAdapter(newsAdapter::retry),
                    footer = ExampleLoadStateAdapter(newsAdapter::retry)
                )
        }

        swipeRefresh.setOnRefreshListener {
        }

    }


    private fun setupRecycler() {
        val recyclerView = bind.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this@NewsView)
        recyclerView.adapter = newsAdapter
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
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


