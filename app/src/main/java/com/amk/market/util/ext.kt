package com.amk.market.util

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.SimpleDateFormat
import java.util.Calendar

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("Error", "error -> ${throwable.message}")
}

fun stylePrice(oldPrice: String): String {
    if (oldPrice.length > 3) {
        val reversed = oldPrice.reversed()
        var newPrice = ""
        for (i in oldPrice.indices) {
            newPrice += reversed[i]

            if ((i + 1) % 3 == 0) {
                newPrice += ','
            }
        }
        val finalPrice = newPrice.reversed()
        if (finalPrice.first() == ',') {
            return finalPrice.substring(1) + "Toman"
        }
        return "$finalPrice Toman"
    }
    return "$oldPrice Toman"
}

@SuppressLint("SimpleDateFormat")
fun styleTime(timeInMillis: Long): String {
    val formatter = SimpleDateFormat("yyyy/MM/dd - hh:mm:ss")
    val calender = Calendar.getInstance()
    calender.timeInMillis = timeInMillis
    return formatter.format(calender.timeInMillis)
}