package com.racepartyapp.shared

import kotlinx.serialization.Serializable

@Serializable
actual class Date : Comparable<Date> {
    actual val timestamp: Long

    actual constructor() {
        timestamp = java.util.Date().time
    }

    actual constructor(time: Long) {
        timestamp = time
    }

    constructor(platformDate: java.util.Date) {
        timestamp = platformDate.time
    }

    override fun compareTo(other: Date): Int {
        return timestamp.compareTo(other.timestamp)
    }

    actual fun getTime(): Long = timestamp

    fun toPlatformDate() = java.util.Date(timestamp)
}