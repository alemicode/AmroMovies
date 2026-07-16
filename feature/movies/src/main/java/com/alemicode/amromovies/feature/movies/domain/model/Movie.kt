package com.alemicode.amromovies.feature.movies.domain.model

import kotlinx.datetime.LocalDate

/** A single entry in the trending list. [posterUrl] is already a full, display-ready URL. */
data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val genres: List<Genre>,
    val popularity: Double,
    val releaseDate: LocalDate?,
)
