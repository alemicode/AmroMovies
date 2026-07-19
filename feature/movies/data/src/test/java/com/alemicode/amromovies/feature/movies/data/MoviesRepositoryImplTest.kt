package com.alemicode.amromovies.feature.movies.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.data.local.LocalMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.local.entity.GenreEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieDetailEntity
import com.alemicode.amromovies.feature.movies.data.local.entity.MovieEntity
import com.alemicode.amromovies.feature.movies.data.remote.RemoteMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreListResponseDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieDetailDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieResultDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.TrendingMoviesResponseDto
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

private val ACTION = GenreEntity(id = 28, name = "Action")
private val COMEDY = GenreEntity(id = 35, name = "Comedy")
private val ACTION_DTO = GenreDto(id = 28, name = "Action")

class MoviesRepositoryImplTest {

    private val remoteDataSource = mockk<RemoteMoviesDataSource>()
    private val localDataSource = mockk<LocalMoviesDataSource>()
    private val trendingMoviesFlow = MutableStateFlow<List<MovieEntity>>(emptyList())
    private lateinit var repository: MoviesRepositoryImpl

    @Before
    fun setUp() {
        every { localDataSource.observeTrendingMovies() } returns trendingMoviesFlow
        coEvery { localDataSource.getGenres() } returns listOf(ACTION, COMEDY)

        repository = MoviesRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
        )
    }

    @Test
    fun `observeTrendingMovies maps cached entities`() = runTest {
        trendingMoviesFlow.value = listOf(
            MovieEntity(
                id = 1,
                title = "Cached Movie",
                posterPath = "/poster.jpg",
                genreIds = listOf(ACTION.id, 999),
                popularity = 10.0,
                releaseDate = "2026-01-01",
            ),
        )

        val movies = observeCurrentTrendingMovies()

        assertThat(movies).hasSize(1)
        assertThat(movies.first().genres.map { it.name }).isEqualTo(listOf("Action"))
    }

    @Test
    fun `refreshTrendingMovies merges all 5 pages and resolves genres`() = runTest {
        // Arrange
        coEvery { remoteDataSource.getGenres() } returns Result.Success(
            GenreListResponseDto(genres = listOf(ACTION_DTO))
        )
        coEvery { remoteDataSource.getTrendingMovies(any()) } answers {
            Result.Success(trendingResponse(page = firstArg()))
        }
        coEvery { localDataSource.insertGenres(any()) } returns Unit
        coEvery { localDataSource.replaceTrendingMovies(any()) } answers {
            trendingMoviesFlow.value = firstArg()
        }

        // Act
        val result = repository.refreshTrendingMovies()

        // Assertion
        assertThat(result).isEqualTo(Result.Success(Unit))
        coVerify(exactly = 1) { localDataSource.replaceTrendingMovies(any()) }
        assertThat(observeCurrentTrendingMovies()).hasSize(5)
    }

    @Test
    fun `refreshTrendingMovies fails without writing anything when genres fail`() = runTest {
        coEvery { remoteDataSource.getGenres() } returns Result.Error(DataError.Network.UNKNOWN)
        coEvery { remoteDataSource.getTrendingMovies(any()) } returns Result.Success(
            trendingResponse(page = 1)
        )

        val result = repository.refreshTrendingMovies()

        assertThat(result).isInstanceOf<Result.Error<DataError>>()
        coVerify(exactly = 0) { localDataSource.replaceTrendingMovies(any()) }
        assertThat(observeCurrentTrendingMovies()).isEmpty()
    }

    @Test
    fun `refreshTrendingMovies fails without a partial write when one page fails`() = runTest {
        coEvery { remoteDataSource.getGenres() } returns Result.Success(
            GenreListResponseDto(genres = listOf(ACTION_DTO)),
        )
        coEvery { remoteDataSource.getTrendingMovies(any()) } returns Result.Success(
            trendingResponse(page = 1)
        )
        coEvery { remoteDataSource.getTrendingMovies(3) } returns Result.Error(DataError.Network.UNKNOWN)

        val result = repository.refreshTrendingMovies()

        assertThat(result).isInstanceOf<Result.Error<DataError>>()
        coVerify(exactly = 0) { localDataSource.replaceTrendingMovies(any()) }
        assertThat(observeCurrentTrendingMovies()).isEmpty()
    }

    @Test
    fun `getMovieDetail on success caches the detail and upserts its genres`() = runTest {
        coEvery { remoteDataSource.getMovieDetail(1) } returns Result.Success(
            movieDetail(id = 1, title = "One", genres = listOf(ACTION_DTO))
        )
        coEvery { localDataSource.insertGenres(any()) } returns Unit
        coEvery { localDataSource.insertMovieDetail(any()) } returns Unit
        coEvery { localDataSource.getMovieDetail(1) } returns movieDetailEntity(
            id = 1,
            title = "One"
        )

        val result = repository.getMovieDetail(1)

        assertThat(result).isInstanceOf<Result.Success<*>>()
        val detail = (result as Result.Success).data
        assertThat(detail.title).isEqualTo("One")
        assertThat(detail.genres.map { it.name }).isEqualTo(listOf("Action"))
        coVerify(exactly = 1) { localDataSource.insertMovieDetail(any()) }
    }

    @Test
    fun `getMovieDetail falls back to the cache when the network call fails`() = runTest {
        coEvery { remoteDataSource.getMovieDetail(1) } returns Result.Error(DataError.Network.NO_INTERNET)
        coEvery { localDataSource.getMovieDetail(1) } returns movieDetailEntity(
            id = 1,
            title = "Cached"
        )

        val result = repository.getMovieDetail(1)

        assertThat(result).isInstanceOf<Result.Success<*>>()
        assertThat((result as Result.Success).data.title).isEqualTo("Cached")
    }

    @Test
    fun `getMovieDetail propagates the error when nothing is cached and the network fails`() =
        runTest {
            coEvery { remoteDataSource.getMovieDetail(999) } returns Result.Error(DataError.Network.NO_INTERNET)
            coEvery { localDataSource.getMovieDetail(999) } returns null

            val result = repository.getMovieDetail(999)

            assertThat(result).isInstanceOf<Result.Error<DataError>>()
        }

    private fun TestScope.observeCurrentTrendingMovies(): List<Movie> {
        var movies: List<Movie>? = null
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.observeTrendingMovies().collect { movies = it }
        }
        return movies ?: error("observeTrendingMovies() never emitted")
    }

    private fun trendingResponse(page: Int) = TrendingMoviesResponseDto(
        page = page,
        results = listOf(movieResult(id = page)),
        totalPages = 500,
    )

    private fun movieResult(
        id: Int,
        title: String = "Movie $id",
        genreIds: List<Int> = listOf(28)
    ) =
        MovieResultDto(
            id = id,
            title = title,
            posterPath = "/poster.jpg",
            genreIds = genreIds,
            popularity = 10.0,
            releaseDate = "2026-01-01",
        )

    private fun movieDetail(
        id: Int,
        title: String = "Movie $id",
        genres: List<GenreDto> = emptyList()
    ) = MovieDetailDto(
        id = id,
        title = title,
        tagline = "Tagline",
        posterPath = "/poster.jpg",
        genres = genres,
        overview = "Overview",
        voteAverage = 7.5,
        voteCount = 100,
        budget = 1_000_000L,
        revenue = 2_000_000L,
        status = "Released",
        imdbId = "tt1234567",
        runtime = 120,
        releaseDate = "2026-01-01",
    )

    private fun movieDetailEntity(id: Int, title: String = "Movie $id") = MovieDetailEntity(
        id = id,
        title = title,
        tagline = "Tagline",
        posterPath = "/poster.jpg",
        genreIds = listOf(28),
        overview = "Overview",
        voteAverage = 7.5,
        voteCount = 100,
        budget = 1_000_000L,
        revenue = 2_000_000L,
        status = "Released",
        imdbId = "tt1234567",
        runtimeMinutes = 120,
        releaseDate = "2026-01-01",
    )
}
