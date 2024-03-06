package com.example.weatherinfo.weather.app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherinfo.BuildConfig
import com.example.weatherinfo.weather.app.data.api.core.WeatherForecastResponse
import com.example.weatherinfo.weather.app.data.api.core.CurrentWeatherResponse
import com.example.weatherinfo.weather.app.data.api.service.ApiUtils
import com.example.weatherinfo.weather.app.data.api.service.WeatherRepository
import com.example.weatherinfo.weather.app.ui.event.MyWeatherEvent
import com.example.weatherinfo.weather.app.utils.EEEE
import com.example.weatherinfo.weather.app.utils.SUCCESS
import com.example.weatherinfo.weather.app.utils.convertToDayFormat
import com.example.weatherinfo.weather.app.utils.getCurrentDayOfWeek
import com.example.weatherinfo.weather.app.utils.yyyy_MM_dd_T_HH_mm_ss
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val getWeatherData = MutableLiveData<CurrentWeatherResponse?>()
    val getForecastData = MutableLiveData<WeatherForecastResponse?>()

    fun getCurrentWeatherAndForecast(city: String, myWeather: MyWeatherEvent) {
        viewModelScope.launch {
            ApiUtils.makeParallelApiCalls(
                { repository.getCurrentWeather(city, BuildConfig.APIKEY) },
                { repository.getWeatherForecast(city, BuildConfig.APIKEY) },
                { currentWeatherData ->
                    getWeatherData.value = currentWeatherData
                    myWeather.showMessage(SUCCESS)
                },
                { weatherForecastData -> getUniqueDay(weatherForecastData) },
                { errorMessage -> myWeather.showMessage(errorMessage) }
            )
        }
    }

    private fun getUniqueDay(response: WeatherForecastResponse) {
        val currentDay = getCurrentDayOfWeek()
        val uniqueDays =
            response.list?.map { it.dt_txt.convertToDayFormat(yyyy_MM_dd_T_HH_mm_ss, EEEE) }
                ?.distinct()?.filter { it != currentDay }
        val filteredForecastData = response.copy(list = uniqueDays?.mapNotNull { day ->
            response.list.find {
                it.dt_txt.convertToDayFormat(
                    yyyy_MM_dd_T_HH_mm_ss,
                    EEEE
                ) == day
            }
        })
        getForecastData.value = filteredForecastData
    }
}