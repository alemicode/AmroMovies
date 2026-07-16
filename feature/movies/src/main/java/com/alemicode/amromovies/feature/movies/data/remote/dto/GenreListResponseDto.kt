package com.alemicode.amromovies.feature.movies.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreListResponseDto(
    val genres: List<GenreDto> = emptyList(),
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String,
)
