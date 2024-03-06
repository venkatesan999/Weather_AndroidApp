package com.example.weatherinfo.weather.app.data.api.service

import com.example.weatherinfo.BuildConfig
import com.example.weatherinfo.weather.app.data.api.core.WeatherForecastResponse
import com.example.weatherinfo.weather.app.data.api.core.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServicesInterface {

    @GET(BuildConfig.BASEURL + WEATHER)
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("APPID") apiKey: String
    ): Response<CurrentWeatherResponse>

    @GET(BuildConfig.BASEURL + FORECAST)
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("APPID") apiKey: String
    ): Response<WeatherForecastResponse>

}