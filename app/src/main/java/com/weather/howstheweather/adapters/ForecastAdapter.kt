package com.weather.howstheweather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.weather.howstheweather.R
import com.weather.howstheweather.models.ForecastWeatherList
import com.weather.howstheweather.models.WeatherOfTheDay
import kotlinx.android.synthetic.main.fragment_current.*
import kotlinx.android.synthetic.main.fragment_current.view.*
import kotlinx.android.synthetic.main.item_weather.view.*
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>(){

    private val diffCallback = object:DiffUtil.ItemCallback<WeatherOfTheDay>() {
        override fun areItemsTheSame(
            oldItem: WeatherOfTheDay,
            newItem: WeatherOfTheDay
        ): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: WeatherOfTheDay,
            newItem: WeatherOfTheDay
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }
    val listDiffer = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false))
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val weather = listDiffer.currentList[position]
        holder.itemView.apply {
//            var temp = "${weather.main?.temp?.toString()}ºC"
//            tvTempHighItem.text = temp

            var dayTemp = "${weather.main?.temp_max?.toString()}ºC"
            tvTempHighItem.text = dayTemp

            var nightTemp = "${weather.main?.temp_min?.toString()}ºC"
            tvTempLowItem.text = nightTemp

//            var tempFeelsLike = "Feels Like ${weather.main?.feels_like?.toString()}ºC"
//            tvTempFeelsLike.text = tempFeelsLike

            weather.weather?.let {
                if(it.size > 0) {
                    val description = it[0].description
                    tvTempDescriptionItem.text = description
                    if(description!= null && description.contains("cloud")) {
                        ivWeatherItem.setImageResource(R.drawable.ic_cloudy)
                    } else if(description != null && description.contains("wind")) {
                        ivWeatherItem.setImageResource(R.drawable.ic_windy)
                    } else {
                        ivWeatherItem.setImageResource(R.drawable.ic_day)
                    }
                }
            }

            //tvLocation.text = weather.name

//            val calendar = Calendar.getInstance()
//            val dateFormat = SimpleDateFormat("EEE, MMM d");
//            val date = dateFormat.format(calendar.time);
//            tvDateItem.text = date
        }

    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size-1
    }

    inner class ForecastViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {}
}