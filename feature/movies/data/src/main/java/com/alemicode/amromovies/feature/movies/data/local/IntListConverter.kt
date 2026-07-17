package com.alemicode.amromovies.feature.movies.data.local

import androidx.room.TypeConverter

internal class IntListConverter {
    @TypeConverter
    fun fromList(value: List<Int>): String = value.joinToString(separator = ",")

    @TypeConverter
    fun toList(value: String): List<Int> =
        if (value.isBlank()) emptyList() else value.split(",").map { it.trim().toInt() }
}
