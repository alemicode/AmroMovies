package com.alemicode.amromovies.feature.movies.data.mapper

import kotlinx.datetime.LocalDate

internal fun String?.toLocalDateOrNull(): LocalDate? =
    if (isNullOrBlank()) null else runCatching { LocalDate.parse(this) }.getOrNull()
