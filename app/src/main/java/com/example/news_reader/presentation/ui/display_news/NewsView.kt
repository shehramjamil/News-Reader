package com.example.news_reader.presentation.ui.display_news

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.news_reader.databinding.ActivityMainBinding
import com.example.news_reader.domain.models.NewsBuisnessModel
import com.example.news_reader.utils.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class NewsView : AppCompatActivity() {

    private val viewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var newsAdapter: NewsAdapter

    private lateinit var bind: ActivityMainBinding
    private lateinit var recyclerViewForNews: RecyclerView
    private lateinit var lazyLoadingProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViews()
        setupRecycler()
        loadFromServer()
        loadAPageFromRoom(0)

        lifecycleScope.launchWhenStarted {
            viewModel.serverResponse.observe(this@NewsView) {
            }
        }
    }

    private fun bindViews() {
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        recyclerViewForNews = bind.recyclerView
        lazyLoadingProgressBar = bind.lazyLoadingProgressBar
    }

    private fun setupRecycler() {
        recyclerViewForNews.layoutManager = LinearLayoutManager(this@NewsView)
        recyclerViewForNews.adapter = newsAdapter
        recyclerViewForNews.addOnScrollListener(scrollListener)

    }

    private fun loadAPageFromRoom(position: Int) {
        recyclerViewForNews.removeOnScrollListener(scrollListener)
        viewModel.getNewsDataFromRoom(position)?.observe(this, newsDataObserver())
    }

    private fun loadFromServer(): Unit = viewModel.getSpecificCountryNewsDataFromServer("us")

    private fun newsDataObserver(): Observer<CustomResponseHandler<List<NewsBuisnessModel>>> {
        return Observer {
            when (it.status) {
                CustomResponseHandler.Status.SUCCESS -> {
                    lazyLoadingProgressBar.isVisible = false
                    newsAdapter.addData(it.data!!)
                    progressBar.visibility = View.GONE
                    recyclerViewForNews.addOnScrollListener(scrollListener)
                }
                CustomResponseHandler.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                CustomResponseHandler.Status.LOADING -> {
                    lazyLoadingProgressBar.isVisible = false
                    Snackbar.make(bind.newsView, "Loading", Snackbar.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                lazyLoadingProgressBar.isVisible = true
                loadAPageFromRoom(recyclerView.layoutManager?.itemCount!!)
            }
        }
    }

}


//Toast.makeText(this@NewsView,recyclerView.layoutManager?.itemCount.toString(),Toast.LENGTH_LONG).show()