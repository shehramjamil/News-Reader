package com.example.news_reader.presentation.ui.display_news

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.news_reader.databinding.ActivityMainBinding
import com.example.news_reader.domain.models.NewsBusinessModel
import com.example.news_reader.presentation.ui.display_news.adapters.ChooseCountryAdapter
import com.example.news_reader.presentation.ui.display_news.adapters.NewsAdapter
import com.example.news_reader.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class NewsView : AppCompatActivity(), ChooseCountryAdapter.OnItemClickListener {

    @Inject
    lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var chooseCountryAdapter: ChooseCountryAdapter

    private val viewModel by viewModels<NewsViewModel>()

    private lateinit var bind: ActivityMainBinding

    private lateinit var recyclerViewForNews: RecyclerView
    private lateinit var recyclerViewForChoosingCountry: RecyclerView

    private lateinit var lazyLoadingPB: ProgressBar
    private lateinit var screenLoadingPB: ProgressBar

    var countryKey = "us"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindViews()
        setupRecyclerForNews()
        setupHorizontalRecyclerForSelectingCountry()

        loadFromServer(countryKey)

        viewModel.serverResponse.observe(this@NewsView, serverResponseObserver())

    }

    private fun bindViews() {
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        recyclerViewForNews = bind.recyclerViewForNews
        recyclerViewForChoosingCountry = bind.recyclerViewForChoosingCountry
        lazyLoadingPB = bind.lazyLoadingProgressBar
        screenLoadingPB = bind.screenLoadingProgressBar
    }

    private fun setupRecyclerForNews() {
        recyclerViewForNews.adapter = newsAdapter
        recyclerViewForNews.addOnScrollListener(scrollListener)
    }

    private fun setupHorizontalRecyclerForSelectingCountry() {
        recyclerViewForChoosingCountry.adapter = chooseCountryAdapter
        recyclerViewForChoosingCountry.layoutManager =
            LinearLayoutManager(this@NewsView, LinearLayoutManager.HORIZONTAL, false)
        chooseCountryAdapter.initialiseListener(this)
    }

    private fun loadFromServer(countryKey: String) =
        viewModel.getSpecificCountryNewsDataFromServer(countryKey)

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                lazyLoadingProgressBar.isVisible = true
                loadAPageFromRoom(recyclerView.layoutManager?.itemCount!!)
            }
        }
    }

    private fun loadAPageFromRoom(position: Int) {
        recyclerViewForNews.removeOnScrollListener(scrollListener)
        viewModel.getSpecificCountryNewsDataFromRoom(position, countryKey)
        viewModel.newsPagedDataFromRoom?.observe(this, roomResponseObserver())
    }

    private fun serverResponseObserver(): Observer<String> {
        return Observer { response ->
            if (!response.equals("Successful")) {
                hideProgressBars()
                Toast.makeText(this, response, Toast.LENGTH_LONG).show()
            }
            loadAPageFromRoom(0)
        }
    }

    private fun roomResponseObserver(): Observer<CustomResponseHandler<List<NewsBusinessModel>>> {
        return Observer { response ->
            when (response.status) {
                CustomResponseHandler.Status.SUCCESS -> {
                    hideProgressBars()
                    val filteredNewsData = viewModel.filterNewsData(countryKey, response.data)
                    newsAdapter.addNewsList(filteredNewsData!!)
                    recyclerViewForNews.addOnScrollListener(scrollListener)
                }
                CustomResponseHandler.Status.ERROR -> {
                    hideProgressBars()
                    //Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                CustomResponseHandler.Status.LOADING -> {
                    hideProgressBars()
                    Toast.makeText(this, "No More Data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun hideProgressBars() {
        screenLoadingPB.isVisible = false
        lazyLoadingProgressBar.isVisible = false
    }

    override fun onItemClick(countryKey: String) {
        this.countryKey = countryKey
        loadFromServer(countryKey)

        newsAdapter.clearNewsList()
        screenLoadingPB.visibility = View.VISIBLE
    }
}



