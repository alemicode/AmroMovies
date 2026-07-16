package com.alemicode.amromovies.feature.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alemicode.amromovies.feature.movies.data.local.entity.GenreEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieDetailEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieEntity

/**
 * Owned entirely by this feature - not a shared `core:database`. Room's @Database class must
 * reference its entity classes directly, so a shared database module would need a dependency
 * back on this feature for [MovieEntity]; keeping it here avoids that inversion.
 */
@Database(
    entities = [
        MovieEntity::class,
        GenreEntity::class,
        MovieDetailEntity::class,
    ],
    version = 1,
)
@TypeConverters(IntListConverter::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}
