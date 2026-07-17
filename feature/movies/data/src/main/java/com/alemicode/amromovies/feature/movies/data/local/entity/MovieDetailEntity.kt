package com.alemicode.amromovies.feature.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetailEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val tagline: String?,
    val posterPath: String?,
    val genreIds: List<Int>,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val budget: Long,
    val revenue: Long,
    val status: String,
    val imdbId: String?,
    val runtimeMinutes: Int?,
    val releaseDate: String?,
)
