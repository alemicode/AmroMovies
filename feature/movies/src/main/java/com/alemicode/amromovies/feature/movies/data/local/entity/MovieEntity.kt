package com.alemicode.amromovies.feature.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** [genreIds] resolve against [GenreEntity] at read time - genres are cached in their own table. */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String?,
    val genreIds: List<Int>,
    val popularity: Double,
    val releaseDate: String?,
)
