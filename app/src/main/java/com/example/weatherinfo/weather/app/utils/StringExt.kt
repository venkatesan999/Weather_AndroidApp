package com.example.weatherinfo.weather.app.utils

fun String?.validateString(): String = this?.takeIf { it.isNotEmpty() }?.trim() ?: ""

