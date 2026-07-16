package com.alemicode.amromovies.feature.movies.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.alemicode.amromovies.designsystem.components.AmroPill
import com.alemicode.amromovies.designsystem.components.AmroPosterImage
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space
import com.alemicode.amromovies.feature.movies.domain.model.Genre
import com.alemicode.amromovies.feature.movies.domain.model.Movie
import kotlinx.datetime.LocalDate

private val PosterWidth = 100.dp
private const val PosterAspectRatio = 2f / 3f

@Composable
fun MovieListItem(
    movie: Movie,
    onClick: (Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onClick(movie) },
        modifier = modifier.fillMaxWidth(),
        shape = AmroShapes.card,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(MaterialTheme.space.space16),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.space16),
        ) {
            AmroPosterImage(
                posterUrl = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(PosterWidth)
                    .aspectRatio(PosterAspectRatio),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space4)) {
                    Text(
                        text = movie.genres.joinToString(" • ") { it.name },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                movie.releaseDate?.let { date ->
                    AmroPill(
                        text = date.year.toString(),
                        modifier = Modifier.align(Alignment.End),
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MovieListItemPreview() {
    AmroTheme {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space16)) {
            MovieListItem(
                movie = Movie(
                    id = 1,
                    title = "Spiderman Across The Spider Verse",
                    posterUrl = null,
                    genres = listOf(
                        Genre(1, "Action"),
                        Genre(2, "Comedy"),
                        Genre(3, "Super Hero"),
                        Genre(4, "Animation"),
                    ),
                    popularity = 900.0,
                    releaseDate = LocalDate(2023, 6, 2),
                ),
                onClick = {},
            )
            MovieListItem(
                movie = Movie(
                    id = 2,
                    title = "Interstellar",
                    posterUrl = null,
                    genres = listOf(
                        Genre(5, "Adventure"),
                        Genre(6, "Mystery"),
                        Genre(7, "Drama"),
                    ),
                    popularity = 700.0,
                    releaseDate = LocalDate(2016, 11, 6),
                ),
                onClick = {},
            )
        }
    }
}
