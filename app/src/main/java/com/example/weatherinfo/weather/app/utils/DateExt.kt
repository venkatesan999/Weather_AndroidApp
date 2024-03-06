package com.example.weatherinfo.weather.app.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val yyyy_MM_dd_T_HH_mm_ss = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
val EEEE = SimpleDateFormat("EEEE", Locale.ENGLISH)

fun String?.convertToDayFormat(
    inputFormat: SimpleDateFormat,
    outputFormat: SimpleDateFormat,
): String = this?.takeIf { isValidString() }
    ?.let { inputFormat.parse(it) }
    ?.let { outputFormat.format(it) }
    ?: ""

fun getCurrentDayOfWeek(): String {
    val calendar = Calendar.getInstance()
    val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return dayFormat.format(calendar.time)
}