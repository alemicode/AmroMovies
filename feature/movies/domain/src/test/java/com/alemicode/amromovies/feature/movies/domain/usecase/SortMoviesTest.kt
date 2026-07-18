package com.alemicode.amromovies.feature.movies.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import kotlinx.datetime.LocalDate
import org.junit.Test

class SortMoviesTest {

    private val zebra = movie(title = "Zebra", popularity = 10.0, releaseDate = "2020-01-01")
    private val apple = movie(title = "Apple", popularity = 30.0, releaseDate = "2023-06-15")
    private val mango = movie(title = "Mango", popularity = 20.0, releaseDate = "2010-12-31")
    private val movies = listOf(zebra, apple, mango)

    @Test
    fun `sorts by popularity ascending`() {
        val result = movies.sortedByField(SortField.POPULARITY, SortOrder.ASCENDING)

        assertThat(result).extracting(Movie::title).containsExactly("Zebra", "Mango", "Apple")
    }

    @Test
    fun `sorts by popularity descending`() {
        val result = movies.sortedByField(SortField.POPULARITY, SortOrder.DESCENDING)

        assertThat(result).extracting(Movie::title).containsExactly("Apple", "Mango", "Zebra")
    }

    @Test
    fun `sorts by title ascending case-insensitively`() {
        val result = movies.sortedByField(SortField.TITLE, SortOrder.ASCENDING)

        assertThat(result).extracting(Movie::title).containsExactly("Apple", "Mango", "Zebra")
    }

    @Test
    fun `sorts by title descending`() {
        val result = movies.sortedByField(SortField.TITLE, SortOrder.DESCENDING)

        assertThat(result).extracting(Movie::title).containsExactly("Zebra", "Mango", "Apple")
    }

    @Test
    fun `sorts by release date ascending`() {
        val result = movies.sortedByField(SortField.RELEASE_DATE, SortOrder.ASCENDING)

        assertThat(result).extracting(Movie::title).containsExactly("Mango", "Zebra", "Apple")
    }

    @Test
    fun `sorts by release date descending`() {
        val result = movies.sortedByField(SortField.RELEASE_DATE, SortOrder.DESCENDING)

        assertThat(result).extracting(Movie::title).containsExactly("Apple", "Zebra", "Mango")
    }

    @Test
    fun `movies with a null release date sort first when ascending`() {
        val noDate = movie(title = "NoDate", popularity = 0.0, releaseDate = null)

        val result = (movies + noDate).sortedByField(SortField.RELEASE_DATE, SortOrder.ASCENDING)

        assertThat(result.first().title).isEqualTo("NoDate")
    }

    private fun movie(title: String, popularity: Double, releaseDate: String?) = Movie(
        id = title.hashCode(),
        title = title,
        posterUrl = null,
        genres = emptyList(),
        popularity = popularity,
        releaseDate = releaseDate?.let(LocalDate::parse),
    )
}
