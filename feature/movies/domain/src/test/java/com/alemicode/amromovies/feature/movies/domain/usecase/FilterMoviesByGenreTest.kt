package com.alemicode.amromovies.feature.movies.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import org.junit.Test

private val ACTION = Genre(id = 28, name = "Action")
private val COMEDY = Genre(id = 35, name = "Comedy")
private val DRAMA = Genre(id = 18, name = "Drama")

class FilterMoviesByGenreTest {

    private val movies = listOf(
        movie(id = 1, title = "Action Movie", genres = listOf(ACTION)),
        movie(id = 2, title = "Comedy Movie", genres = listOf(COMEDY)),
        movie(id = 3, title = "Action Comedy", genres = listOf(ACTION, COMEDY)),
    )

    @Test
    fun `null genre id returns the list unchanged`() {
        val result = movies.filtereMovieByGenre(genreId = null)

        assertThat(result).containsExactly(*movies.toTypedArray())
    }

    @Test
    fun `filters to only movies containing the given genre id`() {
        val result = movies.filtereMovieByGenre(genreId = ACTION.id)

        assertThat(result).containsExactly(movies[0], movies[2])
    }

    @Test
    fun `genre id absent from every movie returns an empty list`() {
        val result = movies.filtereMovieByGenre(genreId = DRAMA.id)

        assertThat(result).isEmpty()
    }

    private fun movie(id: Int, title: String, genres: List<Genre>) = Movie(
        id = id,
        title = title,
        posterUrl = null,
        genres = genres,
        popularity = 0.0,
        releaseDate = null,
    )
}
