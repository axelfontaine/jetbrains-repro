package com.racepartyapp.shared

import kotlinx.serialization.Serializable

@Serializable
expect class Date : Comparable<Date> {
    val timestamp: Long

    /**
     * Get a date at the current instant.
     */
    constructor()

    /**
     * Get a date at the specified instant.
     *
     * @param time: The amount of milliseconds since `1970-01-01T00:00:00Z`.
     */
    constructor(time: Long)

    /**
     * Get the amount of milliseconds since `1970-01-01T00:00:00Z`.
     *
     * @return The amount of milliseconds since reference date.
     */
    fun getTime(): Long
}