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
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                }
                CustomResponseHandler.Status.LOADING -> {
                    Snackbar.make(bind.newsView, "No Data Found", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


}


