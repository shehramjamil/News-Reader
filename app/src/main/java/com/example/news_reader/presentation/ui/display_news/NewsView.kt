package com.example.news_reader.presentation.ui.display_news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news_reader.R
import com.example.news_reader.presentation.ui.NewsViewInterface
import com.example.news_reader.utils.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


@AndroidEntryPoint
class NewsView : AppCompatActivity(),
    NewsViewInterface {

    private val viewModel by viewModels<NewsViewModel>()

    @Inject
    lateinit var newsAdapter: NewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecycler()
        viewModel.fetchNews.observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                NetworkResponse.Status.SUCCESS -> {
                    newsAdapter.addData(it.data!!.articles)
                    progressBar.visibility = View.GONE
                }
                NetworkResponse.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG)
                        .show()
                }
                NetworkResponse.Status.LOADING -> {
                }
            }
        })

    }

    private fun setupRecycler() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this@NewsView)
        recyclerView.adapter = newsAdapter
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }
}


