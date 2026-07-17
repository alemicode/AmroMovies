package com.alemicode.amromovies.feature.movies.data.mapper

import com.alemicode.amromovies.feature.movies.data.local.entity.MovieEntity
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieResultDto
import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie

internal fun MovieResultDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    posterPath = posterPath,
    genreIds = genreIds,
    popularity = popularity,
    releaseDate = releaseDate,
)

internal fun MovieEntity.toDomain(genresById: Map<Int, Genre>): Movie = Movie(
    id = id,
    title = title,
    posterUrl = listPosterUrl(posterPath),
    genres = genreIds.mapNotNull { id ->
        genresById[id]
    },
    popularity = popularity,
    releaseDate = releaseDate.toLocalDateOrNull(),
)
