package com.example.news_reader.presentation.ui.display_news


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.news_reader.R
import com.example.news_reader.domain.models.NewsBuisnessModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NewsAdapter @Inject constructor(
    @ApplicationContext val context: Context
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {


    var list: ArrayList<NewsBuisnessModel> = ArrayList()

    fun addData(data: List<NewsBuisnessModel>) {
        if (!list.containsAll(data)) {
            list.clear()
            list.addAll(data)
        }
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
        holder.storyNumber.text = "Story " + (position + 1).toString()
        holder.newsDescription.text = list[position].newsDescription
        //holder.publishedDateTime.text = list[position].publishedDataTime
        holder.progressBar.visibility = View.VISIBLE

        //val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL) // Optional for storing images
        Glide
            .with(context)
            .load(list[position].imageURL)
            .centerCrop()
            .placeholder(ColorDrawable(Color.BLACK))
            .addListener(glideListener(holder))
            .error(ColorDrawable(Color.DKGRAY))
            .into(holder.image)

    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsDescription: TextView = view.findViewById(R.id.description)
        val image: ImageView = view.findViewById(R.id.image)
        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var publishedDateTime: TextView = view.findViewById(R.id.publishedDataTime)
        val storyNumber: TextView = view.findViewById(R.id.story_number)

        init {
            view.setOnClickListener {
                // val newsData = list.toTypedArray()[adapterPosition]

            }
        }

    }


    private fun glideListener(holder: ViewHolder): RequestListener<Drawable> {
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

    object UserComparator : DiffUtil.ItemCallback<NewsBuisnessModel>() {
        override fun areItemsTheSame(oldItem: NewsBuisnessModel, newItem: NewsBuisnessModel): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsBuisnessModel, newItem: NewsBuisnessModel): Boolean {
            return oldItem == newItem
        }
    }

}
