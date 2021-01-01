package com.example.news_reader.presentation.ui.display_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.news_reader.databinding.ActivityMainBinding
import com.example.news_reader.databinding.NewsAdapterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NewsView : AppCompatActivity() {
    private val viewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var adapter: NewsPagingAdapter
    private lateinit var activityBind: ActivityMainBinding
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar


    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBind.root)
        swipeRefresh = activityBind.swipeRefresh
        progressBar = activityBind.progressBar

        setupRecycler()

        lifecycleScope.launch {
            viewModel.pager.collectLatest {
                adapter.submitData(it)
            }
        }

        // Showing loading states using LoadState Listener[Adapter can also be used]
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                progressBar.isVisible = loadState.append is LoadState.Loading
            if(loadState.prepend.endOfPaginationReached){
                adapter.refresh()
            }
            }
        }

        swipeRefresh.setOnRefreshListener {
            adapter.refresh()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun setupRecycler() {
        val recyclerView = activityBind.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }
}


