package com.alemicode.amromovies.feature.movies.domain.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RefreshTrendingMoviesUseCaseTest {

    @Test
    fun `delegates to the repository and returns its result on success`() = runTest {
        val repository = mockk<MoviesRepository>()
        coEvery { repository.refreshTrendingMovies() } returns Result.Success(Unit)
        val useCase = RefreshTrendingMoviesUseCase(repository)

        val result = useCase()

        assertThat(result).isEqualTo(Result.Success(Unit))
        coVerify(exactly = 1) { repository.refreshTrendingMovies() }
    }

    @Test
    fun `propagates a failure from the repository unchanged`() = runTest {
        val repository = mockk<MoviesRepository>()
        coEvery { repository.refreshTrendingMovies() } returns Result.Error(DataError.Network.NO_INTERNET)
        val useCase = RefreshTrendingMoviesUseCase(repository)

        val result = useCase()

        assertThat(result).isEqualTo(Result.Error(DataError.Network.NO_INTERNET))
    }
}
