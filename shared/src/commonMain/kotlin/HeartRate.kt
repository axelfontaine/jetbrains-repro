package com.racepartyapp.shared

data class HeartRate(val bpm: Int) {
    init {
        if (bpm < 0) throw IllegalArgumentException("Dead person: $this")
        if (bpm > 220) throw IllegalArgumentException("Heart attack: $this")
    }

    override fun toString(): String {
        return "$bpm bpm"
    }
}