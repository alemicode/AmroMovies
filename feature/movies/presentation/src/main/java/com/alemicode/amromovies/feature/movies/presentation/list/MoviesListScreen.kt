package com.alemicode.amromovies.feature.movies.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alemicode.amromovies.designsystem.components.AmroButton
import com.alemicode.amromovies.designsystem.components.AmroThemeToggleButton
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space
import com.alemicode.amromovies.feature.movies.presentation.R
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder
import com.alemicode.amromovies.feature.movies.presentation.list.component.GenreFilterRow
import com.alemicode.amromovies.feature.movies.presentation.list.component.MovieListItem
import com.alemicode.amromovies.feature.movies.presentation.list.component.SortControl
import org.koin.androidx.compose.koinViewModel

private enum class MoviesListContentType { LOADING, ERROR, CONTENT }

@Composable
fun MoviesListRoot(
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    MoviesListScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is MoviesListAction.OnMovieClick -> onMovieClick(action.movieId)
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}

@Composable
fun MoviesListScreen(
    state: MoviesListState,
    onAction: (MoviesListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        val contentType = when {
            state.movies.isEmpty() && state.isLoading -> MoviesListContentType.LOADING
            state.movies.isEmpty() && state.hasError -> MoviesListContentType.ERROR
            else -> MoviesListContentType.CONTENT
        }

        Crossfade(targetState = contentType, label = "moviesListContent") { type ->
            when (type) {
                MoviesListContentType.LOADING -> LoadingContent()
                MoviesListContentType.ERROR -> ErrorContent(
                    onRetry = { onAction(MoviesListAction.OnRetryClick) },
                )
                MoviesListContentType.CONTENT -> MoviesListContent(state = state, onAction = onAction)
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorContent(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
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
}

@Composable
private fun MoviesListContent(
    state: MoviesListState,
    onAction: (MoviesListAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.space.space16,
                    vertical = MaterialTheme.space.space16,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.movies_list_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            AmroThemeToggleButton()
        }

        GenreFilterRow(
            genreFilters = state.genreFilters,
            onGenreSelected = { onAction(MoviesListAction.OnGenreSelected(it)) },
        )

        SortControl(
            sortField = state.sortField,
            sortOrder = state.sortOrder,
            onSortFieldSelected = { onAction(MoviesListAction.OnSortFieldSelected(it)) },
            onToggleSortOrder = { onAction(MoviesListAction.OnToggleSortOrder) },
            modifier = Modifier.padding(
                horizontal = MaterialTheme.space.space16,
                vertical = MaterialTheme.space.space8,
            ),
        )

        if (state.movies.isEmpty()) {
            EmptyListContent(
                hasGenreFilter = state.selectedGenreId != null,
                onShowAllClick = { onAction(MoviesListAction.OnGenreSelected(null)) },
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.space.space16,
                    vertical = MaterialTheme.space.space16,
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space16),
            ) {
                items(state.movies, key = { it.id }) { movie ->
                    MovieListItem(
                        movie = movie,
                        onClick = { onAction(MoviesListAction.OnMovieClick(movie.id)) },
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyListContent(
    hasGenreFilter: Boolean,
    onShowAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.padding(MaterialTheme.space.space32), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.space.space16, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(
                    if (hasGenreFilter) {
                        R.string.movies_list_empty_filtered
                    } else {
                        R.string.movies_list_empty_generic
                    },
                ),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            if (hasGenreFilter) {
                AmroButton(label = stringResource(R.string.action_show_all), onClick = onShowAllClick)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MoviesListScreenContentPreview() {
    AmroTheme {
        MoviesListScreen(
            state = MoviesListState(
                movies = listOf(
                    MovieUi(
                        id = 1,
                        title = "Spiderman Across The Spider Verse",
                        posterUrl = null,
                        genreLabel = "Action • Comedy • Super Hero • Animation",
                        year = "2023",
                    ),
                    MovieUi(
                        id = 2,
                        title = "Interstellar",
                        posterUrl = null,
                        genreLabel = "Adventure • Mystery • Drama",
                        year = "2016",
                    ),
                ),
                genreFilters = listOf(
                    GenreFilterUi(id = null, label = "All", selected = true),
                    GenreFilterUi(id = 1, label = "Action", selected = false),
                    GenreFilterUi(id = 2, label = "Adventure", selected = false),
                ),
                sortField = SortField.POPULARITY,
                sortOrder = SortOrder.DESCENDING,
            ),
            onAction = {},
        )
    }
}

@ThemePreviews
@Composable
private fun MoviesListScreenLoadingPreview() {
    AmroTheme {
        MoviesListScreen(
            state = MoviesListState(isLoading = true),
            onAction = {},
        )
    }
}

@ThemePreviews
@Composable
private fun MoviesListScreenErrorPreview() {
    AmroTheme {
        MoviesListScreen(
            state = MoviesListState(isLoading = false, hasError = true),
            onAction = {},
        )
    }
}

@ThemePreviews
@Composable
private fun MoviesListScreenEmptyFilteredPreview() {
    AmroTheme {
        MoviesListScreen(
            state = MoviesListState(
                movies = emptyList(),
                genreFilters = listOf(
                    GenreFilterUi(id = null, label = "All", selected = false),
                    GenreFilterUi(id = 99, label = "Documentary", selected = true),
                ),
                selectedGenreId = 99,
                isLoading = false,
                hasError = false,
            ),
            onAction = {},
        )
    }
}

@ThemePreviews
@Composable
private fun MoviesListScreenEmptyGenericPreview() {
    AmroTheme {
        MoviesListScreen(
            state = MoviesListState(
                movies = emptyList(),
                isLoading = false,
                hasError = false,
            ),
            onAction = {},
        )
    }
}
