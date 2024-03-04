package com.example.weatherinfo.weather.app.api.service

import buildConfig.BuildConfig
import com.example.weatherinfo.weather.app.api.core.ForecastResponse
import com.example.weatherinfo.weather.app.api.core.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServicesInterface {

    @GET(BuildConfig.BASE_URL + WEATHER)
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("APPID") apiKey: String
    ): Response<WeatherResponse>

    @GET(BuildConfig.BASE_URL + FORECAST)
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("APPID") apiKey: String
    ): Response<ForecastResponse>

}