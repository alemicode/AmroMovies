package com.alemicode.amromovies.feature.movies.data.mapper

import com.alemicode.amromovies.feature.movies.data.local.entity.GenreEntity
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreDto
import com.alemicode.amromovies.feature.movies.domain.model.Genre

internal fun GenreDto.toEntity(): GenreEntity = GenreEntity(id = id, name = name)

internal fun GenreEntity.toDomain(): Genre = Genre(id = id, name = name)
