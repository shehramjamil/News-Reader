package com.example.news_reader.presentation.ui.display_news.adapters


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.news_reader.R
import com.example.news_reader.databinding.ChooseCountryAdapterBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.android.synthetic.main.choose_country_adapter.view.*
import kotlinx.android.synthetic.main.news_adapter.view.*
import javax.inject.Inject

@ActivityScoped
class ChooseCountryAdapter @Inject constructor(@ApplicationContext val context: Context) :
    RecyclerView.Adapter<ChooseCountryAdapter.ViewHolder>() {

    private val countryMapping: MutableMap<String, String> = mutableMapOf(
        "us" to "USA",
        "fr" to "France",
        "de" to "Germany",
        "ca" to "Canada",
        "au" to "Australia",
        "in" to "India",
        "gb" to "UK",
        "il" to "Ireland"
    )
    lateinit var bindAdapter: ChooseCountryAdapterBinding
    lateinit var onItemListener: OnItemClickListener
    var clickPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.choose_country_adapter, parent, false)
        bindAdapter = ChooseCountryAdapterBinding.bind(view)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(position)
        highLightButtonOnClick(position, holder)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val countryName = bindAdapter.countryName

        init {
            countryName.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    clickPosition = position
                    notifyDataSetChanged()
                    onItemListener.onItemClick(countryMapping.keys.elementAt(position))
                }
            }
        }

        fun bindTo(position: Int) {
            countryName.text = countryMapping.values.elementAt(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(countryKey: String)
    }

    override fun getItemCount(): Int {
        return countryMapping.size
    }

    fun initialiseListener(listener: OnItemClickListener) {
        onItemListener = listener
    }

    private fun highLightButtonOnClick(position: Int, holder: ViewHolder) {
        if (clickPosition == position) {
            holder.countryName.setTextColor(Color.GREEN)
        } else {
            holder.countryName.setTextColor(Color.BLACK)
        }
    }

}
