package com.example.news_reader.presentation.ui.display_news.adapters


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.news_reader.R
import com.example.news_reader.databinding.NewsAdapterBinding
import com.example.news_reader.domain.models.NewsBusinessModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.android.synthetic.main.news_adapter.view.*
import javax.inject.Inject

@ActivityScoped
class NewsAdapter @Inject constructor(@ApplicationContext val context: Context) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>(
    ) {

    lateinit var bindAdapter: NewsAdapterBinding
    var newsSet = mutableSetOf<NewsBusinessModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_adapter, parent, false)
        bindAdapter = NewsAdapterBinding.bind(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(position)
    }

    inner class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private val newsDescription = bindAdapter.title
        private val image = bindAdapter.image
        var progressBar = bindAdapter.progressBar
        var publishedDateTime = bindAdapter.publishedDataTime
        var layout = bindAdapter.imageRelativeLayout

        fun bindTo(position: Int) {
            newsDescription.text = newsSet.elementAt(position).title
            //holder.publishedDateTime.text = list[position].publishedDataTime
            progressBar.visibility = View.VISIBLE

            Glide
                .with(context)
                .load(newsSet.elementAt(position).imageURL)
                .centerCrop()
                .placeholder(R.color.black)
                .addListener(glideListener(view))
                .error(R.color.black)
                .into(image)
        }
    }

    private fun glideListener(view: View): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                view.progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                view.progressBar.visibility = View.GONE
                return false
            }

        }

    }

    fun addNewsList(newsList: List<NewsBusinessModel>) {
        newsSet.addAll(newsList)

        if (newsSet.size <= 15)
            notifyDataSetChanged()
    }

    fun clearNewsList() {
        newsSet.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return newsSet.size
    }

}
