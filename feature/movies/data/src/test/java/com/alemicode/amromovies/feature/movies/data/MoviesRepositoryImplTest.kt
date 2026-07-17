package com.alemicode.amromovies.feature.movies.data

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import com.alemicode.amromovies.core.common.DataError
import com.alemicode.amromovies.core.common.Result
import com.alemicode.amromovies.feature.movies.data.local.LocalMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.remote.RemoteMoviesDataSource
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.GenreListResponseDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieDetailDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.MovieResultDto
import com.alemicode.amromovies.feature.movies.data.remote.dto.TrendingMoviesResponseDto
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException

private val ACTION = GenreDto(id = 28, name = "Action")
private val COMEDY = GenreDto(id = 35, name = "Comedy")

class MoviesRepositoryImplTest {

    private val fakeDao = FakeMoviesDao()
    private val fakeApi = FakeTmdbApiService()
    private lateinit var repository: MoviesRepositoryImpl

    @BeforeEach
    fun setUp() {
        repository = MoviesRepositoryImpl(
            remoteDataSource = RemoteMoviesDataSource(fakeApi),
            localDataSource = LocalMoviesDataSource(fakeDao),
        )
    }

    @Test
    fun `refreshTrendingMovies merges all 5 pages and resolves genres`() = runTest {
        fakeApi.genresResponse = GenreListResponseDto(genres = listOf(ACTION, COMEDY))
        fakeApi.trendingPages = fivePagesOf(
            1 to listOf(movieResult(id = 1, title = "One", genreIds = listOf(28))),
            2 to listOf(movieResult(id = 2, title = "Two", genreIds = listOf(35))),
        )

        val result = repository.refreshTrendingMovies()

        assertThat(result).isEqualTo(Result.Success(Unit))
        val movies = observeCurrentTrendingMovies()
        assertThat(movies).hasSize(2)
        assertThat(movies.first { it.id == 1 }.genres.map { it.name }).isEqualTo(listOf("Action"))
        assertThat(movies.first { it.id == 2 }.genres.map { it.name }).isEqualTo(listOf("Comedy"))
    }

    @Test
    fun `refreshTrendingMovies fails without writing anything when genres fail`() = runTest {
        fakeApi.genresError = IOException("no internet")
        fakeApi.trendingPages = fivePagesOf(1 to listOf(movieResult(id = 1)))

        val result = repository.refreshTrendingMovies()

        assertThat(result).isInstanceOf<Result.Error<DataError>>()
        assertThat(observeCurrentTrendingMovies()).isEmpty()
    }

    @Test
    fun `refreshTrendingMovies fails without a partial write when one page fails`() = runTest {
        fakeApi.genresResponse = GenreListResponseDto(genres = listOf(ACTION))
        fakeApi.trendingPages = fivePagesOf(
            1 to listOf(movieResult(id = 1)),
            2 to listOf(movieResult(id = 2)),
        )
        fakeApi.trendingErrorPage = 3

        val result = repository.refreshTrendingMovies()

        assertThat(result).isInstanceOf<Result.Error<DataError>>()
        assertThat(observeCurrentTrendingMovies()).isEmpty()
    }

    @Test
    fun `getMovieDetail on success caches the detail and upserts its genres`() = runTest {
        fakeApi.movieDetailResponses = mapOf(
            1 to movieDetail(id = 1, title = "One", genres = listOf(ACTION)),
        )

        val result = repository.getMovieDetail(1)

        assertThat(result).isInstanceOf<Result.Success<*>>()
        val detail = (result as Result.Success).data
        assertThat(detail.title).isEqualTo("One")
        assertThat(detail.genres.map { it.name }).isEqualTo(listOf("Action"))
        assertThat(fakeDao.getMovieDetail(1)).isNotNull()
    }

    @Test
    fun `getMovieDetail falls back to the cache when the network call fails`() = runTest {
        fakeApi.movieDetailResponses = mapOf(1 to movieDetail(id = 1, title = "Cached"))
        repository.getMovieDetail(1)
        fakeApi.movieDetailError = IOException("no internet")

        val result = repository.getMovieDetail(1)

        assertThat(result).isInstanceOf<Result.Success<*>>()
        assertThat((result as Result.Success).data.title).isEqualTo("Cached")
    }

    @Test
    fun `getMovieDetail propagates the error when nothing is cached and the network fails`() = runTest {
        fakeApi.movieDetailError = IOException("no internet")

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

    private fun fivePagesOf(vararg pages: Pair<Int, List<MovieResultDto>>): Map<Int, TrendingMoviesResponseDto> {
        val provided = pages.toMap()
        return (1..5).associateWith { page ->
            TrendingMoviesResponseDto(page = page, results = provided[page].orEmpty(), totalPages = 500)
        }
    }

    private fun movieResult(id: Int, title: String = "Movie $id", genreIds: List<Int> = emptyList()) = MovieResultDto(
        id = id,
        title = title,
        posterPath = "/poster.jpg",
        genreIds = genreIds,
        popularity = 10.0,
        releaseDate = "2026-01-01",
    )

    private fun movieDetail(id: Int, title: String = "Movie $id", genres: List<GenreDto> = emptyList()) = MovieDetailDto(
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
}
