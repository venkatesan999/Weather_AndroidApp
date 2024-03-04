package com.example.weatherinfo.weather.app.utils

import android.widget.TextView

fun TextView.setTextForTextView(input: String?) {
    text = input?.validateString() ?: ""
}