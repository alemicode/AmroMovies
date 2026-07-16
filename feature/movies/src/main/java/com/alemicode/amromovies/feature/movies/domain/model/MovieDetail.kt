package com.alemicode.amromovies.feature.movies.domain.model

import kotlinx.datetime.LocalDate

/** [posterUrl] and [imdbUrl] are already full, display-ready URLs (or null if TMDB has none). */
data class MovieDetail(
    val id: Int,
    val title: String,
    val tagline: String?,
    val posterUrl: String?,
    val genres: List<Genre>,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val budget: Long,
    val revenue: Long,
    val status: String,
    val imdbUrl: String?,
    val runtimeMinutes: Int?,
    val releaseDate: LocalDate?,
)
