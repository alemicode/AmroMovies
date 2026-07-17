package com.alemicode.amromovies.feature.movies.presentation.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alemicode.amromovies.designsystem.components.AmroFilterChip
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space
import com.alemicode.amromovies.feature.movies.presentation.list.GenreFilterUi

@Composable
fun GenreFilterRow(
    genreFilters: List<GenreFilterUi>,
    onGenreSelected: (Int?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = MaterialTheme.space.space16),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.space8),
    ) {
        items(genreFilters, key = { it.id ?: -1 }) { genreFilter ->
            AmroFilterChip(
                label = genreFilter.label,
                selected = genreFilter.selected,
                onClick = { onGenreSelected(genreFilter.id) },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun GenreFilterRowPreview() {
    AmroTheme {
        GenreFilterRow(
            genreFilters = listOf(
                GenreFilterUi(id = null, label = "All", selected = true),
                GenreFilterUi(id = 1, label = "Action", selected = false),
                GenreFilterUi(id = 2, label = "Comedy", selected = false),
                GenreFilterUi(id = 3, label = "Drama", selected = false),
            ),
            onGenreSelected = {},
        )
    }
}
