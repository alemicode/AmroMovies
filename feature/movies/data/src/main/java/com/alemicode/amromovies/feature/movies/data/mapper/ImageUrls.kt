package com.alemicode.amromovies.feature.movies.data.mapper

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"

private const val POSTER_SIZE_LIST = "w342"

private const val POSTER_SIZE_DETAIL = "w500"

internal fun listPosterUrl(path: String?): String? = path?.let { "$TMDB_IMAGE_BASE_URL$POSTER_SIZE_LIST$it" }

internal fun detailPosterUrl(path: String?): String? = path?.let { "$TMDB_IMAGE_BASE_URL$POSTER_SIZE_DETAIL$it" }
