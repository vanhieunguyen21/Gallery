package com.example.gallery.util

// Get duration in string form: hh:mm:ss
// Truncate hour if 0
fun getDurationString(duration: Long) : String {
    var durationLeft = duration / 1000 // get second from millis
    val second = durationLeft % 60
    durationLeft /= 60
    val minute = durationLeft % 60
    durationLeft /= 60
    val hour = durationLeft

    return if (hour > 0) "%d:%02d:%02d".format(hour, minute, second)
        else "%02d:%02d".format(minute, second)
}