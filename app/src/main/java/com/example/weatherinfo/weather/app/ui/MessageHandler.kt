package com.example.weatherinfo.weather.app.ui

import android.content.Context
import android.widget.Toast

object MessageHandler {
    fun Context.showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
