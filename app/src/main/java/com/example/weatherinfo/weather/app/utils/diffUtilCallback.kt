package com.example.weatherinfo.weather.app.utils

import androidx.recyclerview.widget.DiffUtil

fun <T : Any> diffUtilCallback(
    areItemsTheSame: (T, T) -> Boolean,
    areContentsTheSame: (T, T) -> Boolean
): DiffUtil.ItemCallback<T> = object : DiffUtil.ItemCallback<T>() {

    // Store the lambda expressions in Data members
    private val itemsTheSameFunction: (T, T) -> Boolean = areItemsTheSame
    private val contentsTheSameFunction: (T, T) -> Boolean = areContentsTheSame

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return itemsTheSameFunction(oldItem, newItem) // Use the stored lambda expression
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return contentsTheSameFunction(oldItem, newItem) // Use the stored lambda expression
    }
}