package com.example.weatherinfo.weather.app.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherinfo.R
import com.example.weatherinfo.databinding.ActivityMainBinding
import com.example.weatherinfo.weather.app.ui.adapter.ForecastListAdapter
import com.example.weatherinfo.weather.app.ui.event.MyWeatherEvent
import com.example.weatherinfo.weather.app.ui.util.MessageHandler.showCustomSnackBar
import com.example.weatherinfo.weather.app.utils.NO_INTERNET
import com.example.weatherinfo.weather.app.utils.RETRY
import com.example.weatherinfo.weather.app.utils.SUCCESS
import com.example.weatherinfo.weather.app.utils.isInternetConnected
import com.example.weatherinfo.weather.app.utils.setTextForTextView
import com.example.weatherinfo.weather.app.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MyWeatherEvent {

    private val weatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var bind: ActivityMainBinding
    private lateinit var forecastAdapter: ForecastListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setUpViews()
        if (isInternetConnected(this)) getWeather()
        else this.showMessage(NO_INTERNET)
    }

    private fun setUpViews() {
        with(bind) {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            forecastAdapter = ForecastListAdapter()
            recyclerView.adapter = forecastAdapter
        }
    }

    private fun getWeather() {
        with(bind) {
            setUpAnimation()
            setUpAdapter()
            weatherViewModel.getCurrentWeatherAndForecast("Chennai", this@MainActivity)
            weatherViewModel.getWeatherData.observe(this@MainActivity) { weatherData ->
                currentTempText.setTextForTextView(
                    "${weatherData?.main?.temp?.minus(273.15)?.toInt()}°"
                )
                cityNameText.setTextForTextView(weatherData?.name)
            }
            weatherViewModel.getForecastData.observe(this@MainActivity) { forecastData ->
                forecastAdapter.submitList(forecastData?.list)
            }
        }
    }

    private fun setUpAnimation() {
        with(bind) {
            weatherAndForecastLayout.visibility = View.GONE
            syncIcon.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.rotate)
            syncIcon.startAnimation(rotateAnimation)
        }
    }

    private fun setUpAdapter() {
        with(bind) {
            forecastAdapter = ForecastListAdapter()
            recyclerView.adapter = forecastAdapter
            val animation =
                AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_up_animation)
            recyclerView.startAnimation(animation)
        }
    }

    private fun stopAnimation() {
        with(bind) {
            syncIcon.clearAnimation()
            syncIcon.visibility = View.GONE
        }
    }

    override fun showMessage(message: String?) {
        stopAnimation()
        bind.weatherAndForecastLayout.visibility = View.VISIBLE
        if (!message.equals(SUCCESS))
            message?.let { this.showCustomSnackBar(it, RETRY, Color.RED) { getWeather() } }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(bind) {
            syncIcon.clearAnimation()
            recyclerView.clearAnimation()
        }
    }
}