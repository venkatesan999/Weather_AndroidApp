package com.example.weatherinfo.weather.app.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherinfo.BuildConfig
import com.example.weatherinfo.weather.app.api.core.ForecastResponse
import com.example.weatherinfo.weather.app.api.core.WeatherResponse
import com.example.weatherinfo.weather.app.api.service.WeatherRepository
import com.example.weatherinfo.weather.app.ui.events.MyWeatherEvent
import com.example.weatherinfo.weather.app.utils.EEEE
import com.example.weatherinfo.weather.app.utils.SOCKET_TIMEOUT
import com.example.weatherinfo.weather.app.utils.SOMETHING_WENT_WRONG
import com.example.weatherinfo.weather.app.utils.SUCCESS
import com.example.weatherinfo.weather.app.utils.convertToDayFormat
import com.example.weatherinfo.weather.app.utils.getCurrentDayOfWeek
import com.example.weatherinfo.weather.app.utils.yyyy_MM_dd_T_HH_mm_ss
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    val getWeatherData = MutableLiveData<WeatherResponse?>()
    val getForecastData = MutableLiveData<ForecastResponse?>()

    fun getCurrentWeatherAndForecast(city: String, myWeather: MyWeatherEvent) {
        viewModelScope.launch {
            try {
                val weatherData =
                    async { repository.getCurrentWeather(city, BuildConfig.APIKEY) }.await()
                val forecastData =
                    async { repository.getWeatherForecast(city, BuildConfig.APIKEY) }.await()

                if (weatherData.isSuccessful && forecastData.isSuccessful) {
                    getWeatherData.value = weatherData.body()
                    getUniqueDay(forecastData.body())
                    myWeather.showMessage(SUCCESS)
                } else {
                    myWeather.showMessage(SOMETHING_WENT_WRONG)
                }
            } catch (e: SocketTimeoutException) {
                myWeather.showMessage(SOCKET_TIMEOUT)
            } catch (e: Exception) {
                myWeather.showMessage(SOMETHING_WENT_WRONG)
            }
        }
    }

    private fun getUniqueDay(response: ForecastResponse?) {
        val currentDay = getCurrentDayOfWeek()
        val uniqueDays =
            response?.list?.map { it.dt_txt.convertToDayFormat(yyyy_MM_dd_T_HH_mm_ss, EEEE) }
                ?.distinct()?.filter { it != currentDay }
        val filteredForecastData = response?.copy(list = uniqueDays?.mapNotNull { day ->
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