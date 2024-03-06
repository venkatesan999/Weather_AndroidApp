package com.example.weatherinfo.weather.app.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherinfo.R
import com.example.weatherinfo.databinding.ActivityMainBinding
import com.example.weatherinfo.weather.app.ui.adapter.ForecastListAdapter
import com.example.weatherinfo.weather.app.ui.event.MyWeatherEvent
import com.example.weatherinfo.weather.app.utils.NO_INTERNET
import com.example.weatherinfo.weather.app.utils.RETRY
import com.example.weatherinfo.weather.app.utils.SOCKET_TIMEOUT
import com.example.weatherinfo.weather.app.utils.SUCCESS
import com.example.weatherinfo.weather.app.utils.isInternetConnected
import com.example.weatherinfo.weather.app.utils.setTextForTextView
import com.example.weatherinfo.weather.app.viewModels.WeatherViewModel
import com.google.android.material.snackbar.Snackbar
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
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        if (isInternetConnected(this)) {
            getWeather()
        } else {
            this@MainActivity.showMessage(NO_INTERNET)
        }
    }

    private fun getWeather() {
        with(bind) {
            sycAnimSetUp()
            adapterSetUp()
            weatherViewModel.getCurrentWeatherAndForecast("Bengaluru", this@MainActivity)
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

    private fun sycAnimSetUp() {
        with(bind) {
            weatherAndForecastLayout.visibility = View.GONE
            syncIcon.visibility = View.VISIBLE
            val rotateAnimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.rotate)
            syncIcon.startAnimation(rotateAnimation)
        }
    }

    private fun adapterSetUp() {
        with(bind) {
            forecastAdapter = ForecastListAdapter()
            recyclerView.adapter = forecastAdapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            val animation =
                AnimationUtils.loadAnimation(this@MainActivity, R.anim.slide_up_animation)
            recyclerView.startAnimation(animation)
        }
    }

    private fun stopAnimation() {
        bind.syncIcon.clearAnimation()
        bind.syncIcon.visibility = View.GONE
    }

    override fun showMessage(message: String?) {
        stopAnimation()
        bind.weatherAndForecastLayout.visibility = View.VISIBLE
        if (message.equals(SOCKET_TIMEOUT)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } else if (!message.equals(SUCCESS)) {
            val snackBar = Snackbar.make(
                findViewById(android.R.id.content),
                message ?: "",
                Snackbar.LENGTH_INDEFINITE
            )
            val spannableActionText = SpannableString(RETRY)
            spannableActionText.setSpan(
                ForegroundColorSpan(Color.RED),
                0,
                spannableActionText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            snackBar.setAction(spannableActionText) {
                if (isInternetConnected(this)) getWeather()
                else this.showMessage(message)
                snackBar.dismiss()
            }
            snackBar.show()
        }
    }
}