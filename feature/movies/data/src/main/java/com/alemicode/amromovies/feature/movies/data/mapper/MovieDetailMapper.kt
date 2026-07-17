package com.alemicode.amromovies.feature.movies.data.mapper

import com.alemicode.amromovies.feature.movies.data.local.entity.MovieDetailEntity
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieDetailDto
import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail

internal fun MovieDetailDto.toEntity(): MovieDetailEntity = MovieDetailEntity(
    id = id,
    title = title,
    tagline = tagline,
    posterPath = posterPath,
    genreIds = genres.map { it.id },
    overview = overview.orEmpty(),
    voteAverage = voteAverage,
    voteCount = voteCount,
    budget = budget,
    revenue = revenue,
    status = status.orEmpty(),
    imdbId = imdbId,
    runtimeMinutes = runtime,
    releaseDate = releaseDate,
)

internal fun MovieDetailEntity.toDomain(genresById: Map<Int, Genre>): MovieDetail = MovieDetail(
    id = id,
    title = title,
    tagline = tagline,
    posterUrl = detailPosterUrl(posterPath),
    genres = genreIds.mapNotNull(genresById::get),
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount,
    budget = budget,
    revenue = revenue,
    status = status,
    imdbUrl = imdbId?.takeIf { it.isNotBlank() }?.let { "https://www.imdb.com/title/$it" },
    runtimeMinutes = runtimeMinutes,
    releaseDate = releaseDate.toLocalDateOrNull(),
)
