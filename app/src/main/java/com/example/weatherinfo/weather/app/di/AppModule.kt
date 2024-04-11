package com.example.weatherinfo.weather.app.di

import com.example.weatherinfo.BuildConfig
import com.example.weatherinfo.weather.app.data.api.apiService.WeatherRepository
import com.example.weatherinfo.weather.app.data.api.apiService.WeatherServicesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module // Provides dependencies for an Android app
@InstallIn(SingletonComponent::class)  // Singleton scope for a single instance will be created
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {  // For making network request
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .readTimeout(30, TimeUnit.SECONDS) // 30 seconds for reading from the server
            .callTimeout(30, TimeUnit.SECONDS) // 30 seconds for the overall call
            .connectTimeout(30, TimeUnit.SECONDS) // 30 seconds for establishing a connection
            .writeTimeout(30, TimeUnit.SECONDS) // 30 seconds for writing to the server
            .retryOnConnectionFailure(true)
            .connectionPool(
                ConnectionPool(
                    5,
                    5,
                    TimeUnit.MINUTES
                )
            ) // 5 connections with a 5-minute keep-alive
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherServicesInterface {  // Defining API endpoints related to weather data
        return retrofit.create(WeatherServicesInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherService: WeatherServicesInterface): WeatherRepository {  // WeatherServicesInterface to interact with the weather API
        return WeatherRepository(weatherService)
    }
}