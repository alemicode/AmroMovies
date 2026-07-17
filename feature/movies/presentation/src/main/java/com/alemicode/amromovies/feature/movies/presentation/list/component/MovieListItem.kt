package com.alemicode.amromovies.feature.movies.presentation.list.component

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
import com.alemicode.amromovies.feature.movies.presentation.list.MovieUi

private val PosterWidth = 100.dp
private const val PosterAspectRatio = 2f / 3f

@Composable
fun MovieListItem(
    movie: MovieUi,
    onClick: (MovieUi) -> Unit,
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
                        text = movie.genreLabel,
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

                movie.year?.let { year ->
                    AmroPill(
                        text = year,
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
                movie = MovieUi(
                    id = 1,
                    title = "Spiderman Across The Spider Verse",
                    posterUrl = null,
                    genreLabel = "Action • Comedy • Super Hero • Animation",
                    year = "2023",
                ),
                onClick = {},
            )
            MovieListItem(
                movie = MovieUi(
                    id = 2,
                    title = "Interstellar",
                    posterUrl = null,
                    genreLabel = "Adventure • Mystery • Drama",
                    year = "2016",
                ),
                onClick = {},
            )
        }
    }
}
