package com.example.news_reader.presentation.ui.display_news


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_reader.R
import com.example.news_reader.data.model.json.ArticlesItem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NewsAdapter @Inject constructor(
        @ApplicationContext val context: Context
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {


    var list: ArrayList<ArticlesItem> = ArrayList()

    fun addData(data: ArrayList<ArticlesItem>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.news_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.newsDescription.text = list[position].description

        //Glide Image Downloader
        Glide
            .with(context)
            .load(list[position].urlToImage)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.image);
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsDescription: TextView = view.findViewById(R.id.description)
        val image: ImageView = view.findViewById(R.id.image)

        init {
            view.setOnClickListener {
                val newsData = list.toTypedArray()[adapterPosition]

            }
        }

    }

}
