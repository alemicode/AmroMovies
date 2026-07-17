package com.alemicode.amromovies.feature.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alemicode.amromovies.feature.movies.data.local.entity.GenreEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieDetailEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieEntity

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
