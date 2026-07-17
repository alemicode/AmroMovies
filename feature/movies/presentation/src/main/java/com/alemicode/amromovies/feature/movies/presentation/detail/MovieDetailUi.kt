package com.alemicode.amromovies.feature.movies.presentation.detail

import com.alemicode.amromovies.feature.movies.domain.model.MovieDetail
import java.text.NumberFormat
import java.util.Locale

private val currencyFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
    maximumFractionDigits = 0
}
private val integerFormat: NumberFormat = NumberFormat.getIntegerInstance(Locale.US)

data class MovieDetailUi(
    val id: Int,
    val title: String,
    val tagline: String?,
    val posterUrl: String?,
    val genreLabel: String,
    val overview: String,
    val voteAverage: String,
    val voteCount: String,
    val budget: String?,
    val revenue: String?,
    val status: String,
    val imdbUrl: String?,
    val runtime: String?,
    val releaseDate: String?,
)

fun MovieDetail.toMovieDetailUi(): MovieDetailUi = MovieDetailUi(
    id = id,
    title = title,
    tagline = tagline?.takeIf { it.isNotBlank() },
    posterUrl = posterUrl,
    genreLabel = genres.joinToString(" • ") { it.name },
    overview = overview,
    voteAverage = String.format(Locale.US, "%.1f", voteAverage),
    voteCount = integerFormat.format(voteCount),
    budget = budget.takeIf { it > 0 }?.let(currencyFormat::format),
    revenue = revenue.takeIf { it > 0 }?.let(currencyFormat::format),
    status = status,
    imdbUrl = imdbUrl,
    runtime = runtimeMinutes?.takeIf { it > 0 }?.let { "${it / 60}h ${it % 60}m" },
    releaseDate = releaseDate?.let {
        "${it.month.name.lowercase().replaceFirstChar(Char::uppercase)} ${it.day}, ${it.year}"
    },
)
