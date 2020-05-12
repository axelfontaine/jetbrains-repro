package com.racepartyapp.shared

import kotlinx.serialization.Serializable

@Serializable
actual class Date : Comparable<Date> {
    actual val timestamp: Long

    actual constructor() {
        timestamp = kotlin.js.Date().getTime().toLong()
    }

    actual constructor(time: Long) {
        timestamp = time
    }

    constructor(platformDate: kotlin.js.Date) {
        timestamp = platformDate.getTime().toLong()
    }

    override fun compareTo(other: Date): Int {
        return timestamp.compareTo(other.timestamp)
    }

    actual fun getTime(): Long = timestamp

    fun toPlatformDate() = kotlin.js.Date(timestamp)
}