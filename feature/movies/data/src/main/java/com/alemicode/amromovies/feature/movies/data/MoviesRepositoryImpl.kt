package com.alemicode.amromovies.feature.movies.data

import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.data.local.LocalMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.mapper.toDomain
import com.alemicode.amromovies.feature.movies.data.mapper.toEntity
import com.alemicode.amromovies.feature.movies.data.remote.RemoteMoviesDataSource
import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map as mapFlow

private const val TRENDING_PAGE_COUNT = 5

internal class MoviesRepositoryImpl(
    private val remoteDataSource: RemoteMoviesDataSource,
    private val localDataSource: LocalMoviesDataSource,
) : MoviesRepository {

    override fun observeTrendingMovies(): Flow<List<Movie>> =
        localDataSource.observeTrendingMovies().mapFlow { movieEntities ->
            val genresById = genresById()
            movieEntities.map { it.toDomain(genresById) }
        }

    override suspend fun refreshTrendingMovies(): Result<Unit, DataError> = coroutineScope {
        val genresDeferred = async { remoteDataSource.getGenres() }
        val pageDeferredList = (1..TRENDING_PAGE_COUNT).map { page ->
            async { remoteDataSource.getTrendingMovies(page) }
        }

        val genresDto = when (val genresResult = genresDeferred.await()) {
            is Result.Error -> return@coroutineScope Result.Error(genresResult.error)
            is Result.Success -> genresResult.data
        }

        val pageResults = pageDeferredList.awaitAll()
        val firstFailure = pageResults.firstNotNullOfOrNull { (it as? Result.Error)?.error }
        if (firstFailure != null) return@coroutineScope Result.Error(firstFailure)

        val movies = pageResults.flatMap { pageResult ->
            (pageResult as Result.Success).data.results
        }

        localDataSource.insertGenres(genresDto.genres.map { it.toEntity() })
        localDataSource.replaceTrendingMovies(movies.map { it.toEntity() })
        Result.Success(Unit)
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail, DataError> {
        when (val remoteResult = remoteDataSource.getMovieDetail(movieId)) {
            is Result.Success -> {
                localDataSource.insertGenres(remoteResult.data.genres.map { it.toEntity() })
                localDataSource.insertMovieDetail(remoteResult.data.toEntity())
            }

            is Result.Error -> {
                if (localDataSource.getMovieDetail(movieId) == null) return Result.Error(
                    remoteResult.error
                )
            }
        }

        val cached = localDataSource.getMovieDetail(movieId)
            ?: return Result.Error(DataError.Network.UNKNOWN)
        return Result.Success(cached.toDomain(genresById()))
    }

    private suspend fun genresById(): Map<Int, Genre> =
        localDataSource.getGenres().associate { it.id to it.toDomain() }
}
