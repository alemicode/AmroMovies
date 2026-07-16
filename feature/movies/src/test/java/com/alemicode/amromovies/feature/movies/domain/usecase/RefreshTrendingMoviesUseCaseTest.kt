package com.alemicode.amromovies.feature.movies.domain.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

private class FakeMoviesRepository(private val refreshResult: Result<Unit, DataError>) : MoviesRepository {
    var refreshCallCount = 0
        private set

    override fun observeTrendingMovies(): Flow<List<Movie>> = flowOf(emptyList())

    override suspend fun refreshTrendingMovies(): Result<Unit, DataError> {
        refreshCallCount++
        return refreshResult
    }

    override suspend fun getMovieDetail(movieId: Int): Result<MovieDetail, DataError> =
        error("not used in this test")
}

class RefreshTrendingMoviesUseCaseTest {

    @Test
    fun `delegates to the repository and returns its result on success`() = runTest {
        val repository = FakeMoviesRepository(refreshResult = Result.Success(Unit))
        val useCase = RefreshTrendingMoviesUseCase(repository)

        val result = useCase()

        assertThat(result).isEqualTo(Result.Success(Unit))
        assertThat(repository.refreshCallCount).isEqualTo(1)
    }

    @Test
    fun `propagates a failure from the repository unchanged`() = runTest {
        val repository = FakeMoviesRepository(refreshResult = Result.Error(DataError.Network.NO_INTERNET))
        val useCase = RefreshTrendingMoviesUseCase(repository)

        val result = useCase()

        assertThat(result).isEqualTo(Result.Error(DataError.Network.NO_INTERNET))
    }
}
