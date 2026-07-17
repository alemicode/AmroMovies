package com.alemicode.amromovies.feature.movies.presentation.detail

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alemicode.amromovies.designsystem.components.AmroButton
import com.alemicode.amromovies.designsystem.components.AmroPosterImage
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space
import com.alemicode.amromovies.feature.movies.presentation.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private enum class MovieDetailContentType { LOADING, ERROR, CONTENT }

@Composable
fun MovieDetailRoot(
    movieId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = koinViewModel { parametersOf(movieId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MovieDetailScreen(
        state = state,
        onAction = viewModel::onAction,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
fun MovieDetailScreen(
    state: MovieDetailState,
    onAction: (MovieDetailAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val contentType = when {
            state.movie == null && state.isLoading -> MovieDetailContentType.LOADING
            state.movie == null && state.hasError -> MovieDetailContentType.ERROR
            else -> MovieDetailContentType.CONTENT
        }

        Crossfade(targetState = contentType, label = "movieDetailContent") { type ->
            when (type) {
                MovieDetailContentType.LOADING -> LoadingContent(onBackClick = onBackClick)
                MovieDetailContentType.ERROR -> ErrorContent(
                    onBackClick = onBackClick,
                    onRetry = { onAction(MovieDetailAction.OnRetryClick) },
                )
                MovieDetailContentType.CONTENT -> state.movie?.let { movie ->
                    MovieDetailContent(movie = movie, onBackClick = onBackClick)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        BackButton(onBackClick = onBackClick, modifier = Modifier.align(Alignment.TopStart))
    }
}

@Composable
private fun ErrorContent(onBackClick: () -> Unit, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.space.space32),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space16, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.generic_error_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.generic_error_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AmroButton(label = stringResource(R.string.action_retry), onClick = onRetry)
        }
        BackButton(onBackClick = onBackClick, modifier = Modifier.align(Alignment.TopStart))
    }
}

@Composable
private fun MovieDetailContent(
    movie: MovieDetailUi,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box {
            AmroPosterImage(
                posterUrl = movie.posterUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
            )
            BackButton(onBackClick = onBackClick, modifier = Modifier.align(Alignment.TopStart))
        }

        Column(
            modifier = Modifier.padding(MaterialTheme.space.space16),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space16),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space4)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                movie.tagline?.let { tagline ->
                    Text(
                        text = tagline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontStyle = FontStyle.Italic,
                    )
                }
                Text(
                    text = movie.genreLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = "★ ${movie.voteAverage}  •  ${stringResource(R.string.movie_detail_vote_count_format, movie.voteCount)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space8)) {
                Text(
                    text = stringResource(R.string.movie_detail_overview_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Surface(
                shape = AmroShapes.card,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.space.space16),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space8),
                ) {
                    DetailRow(stringResource(R.string.movie_detail_status_label), movie.status)
                    DetailRow(
                        stringResource(R.string.movie_detail_runtime_label),
                        movie.runtime ?: stringResource(R.string.movie_detail_not_available),
                    )
                    DetailRow(
                        stringResource(R.string.movie_detail_release_date_label),
                        movie.releaseDate ?: stringResource(R.string.movie_detail_not_available),
                    )
                    DetailRow(
                        stringResource(R.string.movie_detail_budget_label),
                        movie.budget ?: stringResource(R.string.movie_detail_not_available),
                    )
                    DetailRow(
                        stringResource(R.string.movie_detail_revenue_label),
                        movie.revenue ?: stringResource(R.string.movie_detail_not_available),
                    )
                }
            }

            movie.imdbUrl?.let { imdbUrl ->
                AmroButton(
                    label = stringResource(R.string.movie_detail_view_on_imdb),
                    onClick = { uriHandler.openUri(imdbUrl) },
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space2)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun BackButton(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onBackClick,
        modifier = modifier
            .padding(MaterialTheme.space.space8)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f), CircleShape)
            .statusBarsPadding(),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.cd_navigate_back),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenContentPreview() {
    AmroTheme {
        MovieDetailScreen(
            state = MovieDetailState(
                movie = MovieDetailUi(
                    id = 1,
                    title = "Interstellar",
                    tagline = "Mankind was born on Earth. It was never meant to die here.",
                    posterUrl = null,
                    genreLabel = "Adventure • Mystery • Drama",
                    overview = "A team of explorers travel through a wormhole in space in an " +
                        "attempt to ensure humanity's survival.",
                    voteAverage = "8.7",
                    voteCount = "32,145",
                    budget = "$165,000,000",
                    revenue = "$677,000,000",
                    status = "Released",
                    imdbUrl = "https://www.imdb.com/title/tt0816692",
                    runtime = "2h 49m",
                    releaseDate = "Nov 6, 2014",
                ),
                isLoading = false,
                hasError = false,
            ),
            onAction = {},
            onBackClick = {},
        )
    }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenLoadingPreview() {
    AmroTheme {
        MovieDetailScreen(
            state = MovieDetailState(isLoading = true),
            onAction = {},
            onBackClick = {},
        )
    }
}

@ThemePreviews
@Composable
private fun MovieDetailScreenErrorPreview() {
    AmroTheme {
        MovieDetailScreen(
            state = MovieDetailState(isLoading = false, hasError = true),
            onAction = {},
            onBackClick = {},
        )
    }
}
