package com.alemicode.amromovies.feature.movies.data.mapper

import kotlinx.datetime.LocalDate

/** TMDB returns either a null or a blank string for movies with no known release date. */
internal fun String?.toLocalDateOrNull(): LocalDate? =
    if (isNullOrBlank()) null else runCatching { LocalDate.parse(this) }.getOrNull()
