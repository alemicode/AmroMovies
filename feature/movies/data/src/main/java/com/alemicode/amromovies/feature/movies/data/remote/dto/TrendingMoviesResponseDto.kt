package com.alemicode.amromovies.feature.movies.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingMoviesResponseDto(
    val page: Int,
    val results: List<MovieResultDto> = emptyList(),
    @SerialName("total_pages") val totalPages: Int = 0,
)

@Serializable
data class MovieResultDto(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int> = emptyList(),
    val popularity: Double = 0.0,
    @SerialName("release_date") val releaseDate: String? = null,
)
