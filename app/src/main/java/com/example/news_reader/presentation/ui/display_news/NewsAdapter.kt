package com.example.news_reader.presentation.ui.display_news


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.news_reader.R
import com.example.news_reader.data.model.room.News
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NewsAdapter @Inject constructor(
    @ApplicationContext val context: Context
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {


    var list: ArrayList<News> = ArrayList()

    fun addData(data: List<News>) {
        list.clear()
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
        holder.newsDescription.text = list[position].newsDescription


        //val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) // Optional for storing images
        Glide
            .with(context)
            .load(list[position].imageURL)
            .centerCrop()
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.image)

        holder.progressBar.visibility = View.GONE
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsDescription: TextView = view.findViewById(R.id.description)
        val image: ImageView = view.findViewById(R.id.image)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        init {
            view.setOnClickListener {
                val newsData = list.toTypedArray()[adapterPosition]

            }
        }

    }

}
