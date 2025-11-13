package com.example.kuit6_android_api.util

fun formatDateTime(dateTime: String): String {
    return try {
        val parts = dateTime.split("T")
        if (parts.size == 2) {
            val date = parts[0]
            val time = parts[1].substring(0, 5)
            "$date $time"
        } else {
            dateTime
        }
    } catch (e: Exception) {
        dateTime
    }
}
