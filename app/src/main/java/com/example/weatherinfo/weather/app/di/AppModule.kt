package com.example.weatherinfo.weather.app.di

import buildConfig.BuildConfig
import com.example.weatherinfo.weather.app.api.service.WeatherRepository
import com.example.weatherinfo.weather.app.api.service.WeatherServicesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherServicesInterface {
        return retrofit.create(WeatherServicesInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherService: WeatherServicesInterface): WeatherRepository {
        return WeatherRepository(weatherService)
    }
}
