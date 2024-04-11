package com.example.weatherinfo.weather.app.data.api.apiService

import com.example.weatherinfo.weather.app.data.api.core.WeatherForecastResponse
import com.example.weatherinfo.weather.app.data.api.core.CurrentWeatherResponse
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherServiceInterface: WeatherServicesInterface) {
    suspend fun getCurrentWeather(city: String, apiKey: String): Response<CurrentWeatherResponse> =
        weatherServiceInterface.getCurrentWeather(city, apiKey)

    suspend fun getWeatherForecast(city: String, apiKey: String): Response<WeatherForecastResponse> =
        weatherServiceInterface.getWeatherForecast(city, apiKey)
}
