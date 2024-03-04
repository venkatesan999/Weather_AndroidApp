package com.example.weatherinfo.weather.app.api.service

import com.example.weatherinfo.weather.app.api.core.ForecastResponse
import com.example.weatherinfo.weather.app.api.core.WeatherResponse
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherServiceInterface: WeatherServicesInterface) {
    suspend fun getCurrentWeather(city: String, apiKey: String): Response<WeatherResponse> =
        weatherServiceInterface.getCurrentWeather(city, apiKey)

    suspend fun getWeatherForecast(city: String, apiKey: String): Response<ForecastResponse> =
        weatherServiceInterface.getWeatherForecast(city, apiKey)
}
