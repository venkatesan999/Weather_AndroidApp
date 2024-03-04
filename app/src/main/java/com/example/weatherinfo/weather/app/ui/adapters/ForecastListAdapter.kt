package com.example.weatherinfo.weather.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherinfo.databinding.ForecastListAdapterBinding
import com.example.weatherinfo.weather.app.api.core.ForecastLists
import com.example.weatherinfo.weather.app.utils.EEEE
import com.example.weatherinfo.weather.app.utils.convertToDayFormat
import com.example.weatherinfo.weather.app.utils.diffUtilCallback
import com.example.weatherinfo.weather.app.utils.setTextForTextView
import com.example.weatherinfo.weather.app.utils.yyyy_MM_dd_T_HH_mm_ss

class ForecastListAdapter : ListAdapter<ForecastLists, ForecastListAdapter.ViewHolder>(
    diffUtilCallback<ForecastLists>(
        areItemsTheSame = { oldItem, newItem -> oldItem.dt_txt == newItem.dt_txt },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem })
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ForecastListAdapterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(private val binding: ForecastListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forecastWeather: ForecastLists) {
            with(binding) {
                dayText.text =
                    forecastWeather.dt_txt.convertToDayFormat(yyyy_MM_dd_T_HH_mm_ss, EEEE)
                tempText.setTextForTextView("${forecastWeather.main?.temp?.minus(273.15)?.toInt()} C")
            }
        }
    }
}