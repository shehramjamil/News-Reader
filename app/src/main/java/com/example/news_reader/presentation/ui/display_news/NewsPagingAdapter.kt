package com.example.news_reader.presentation.ui.display_news


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.news_reader.R
import com.example.news_reader.databinding.ActivityMainBinding
import com.example.news_reader.databinding.NewsAdapterBinding
import com.example.news_reader.domain.models.NewsBuisnessModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NewsPagingAdapter @Inject constructor(
    @ApplicationContext val context: Context
) : PagingDataAdapter<NewsBuisnessModel, NewsPagingAdapter.NewsViewHolder>(NewsComparator) {

    private lateinit var binding: NewsAdapterBinding

    inner class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsDescription = binding.description
        val image = binding.image
        var progressBar = binding.progressBar
        var publishedDateTime = binding.publishedDataTime

        init {
            view.setOnClickListener {
                // val newsData = list.toTypedArray()[adapterPosition]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_adapter, parent, false)
        binding = NewsAdapterBinding.bind(view)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.newsDescription.text = getItem(position)?.newsDescription
        holder.publishedDateTime.text = getItem(position)?.publishedDataTime
        holder.progressBar.visibility = View.VISIBLE

        //val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) // Optional for storing images
        Glide
            .with(holder.itemView.context)
            .load(getItem(position)?.imageURL)
            .centerCrop()
            .placeholder(ColorDrawable(Color.BLACK))
            .addListener(glideListener(holder))
            .error(ColorDrawable(Color.DKGRAY))
            .into(holder.image)

    }


    object NewsComparator : DiffUtil.ItemCallback<NewsBuisnessModel>() {
        override fun areItemsTheSame(
            oldItem: NewsBuisnessModel,
            newItem: NewsBuisnessModel
        ): Boolean {
            // Id is unique.
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: NewsBuisnessModel,
            newItem: NewsBuisnessModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private fun glideListener(holder: NewsViewHolder): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                holder.progressBar.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.progressBar.visibility = View.GONE
                return false
            }

        }

    }


}
