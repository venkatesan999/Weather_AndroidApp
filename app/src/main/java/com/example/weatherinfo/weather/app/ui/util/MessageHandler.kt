package com.example.weatherinfo.weather.app.ui.util

import android.app.Activity
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.weatherinfo.weather.app.utils.isInternetConnected
import com.google.android.material.snackbar.Snackbar

object MessageHandler {

    fun Context.showCustomSnackBar(
        message: String?,
        actionText: String,
        actionTextColor: Int,
        action: () -> Unit
    ) {
        val snackBar = Snackbar.make(
            (this as Activity).findViewById(android.R.id.content),
            message ?: "",
            Snackbar.LENGTH_INDEFINITE
        )
        val spannableActionText = SpannableString(actionText)
        spannableActionText.setSpan(
            ForegroundColorSpan(actionTextColor),
            0,
            spannableActionText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        snackBar.setAction(spannableActionText) {
            snackBar.dismiss()
            if (isInternetConnected(this)) action.invoke()
            else showCustomSnackBar(message, actionText, actionTextColor, action)
        }
        snackBar.show()
    }
}
