package com.alemicode.amromovies.feature.movies.presentation.list.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import com.alemicode.amromovies.designsystem.theme.AmroShapes
import com.alemicode.amromovies.designsystem.theme.AmroTheme
import com.alemicode.amromovies.designsystem.theme.ThemePreviews
import com.alemicode.amromovies.designsystem.theme.space
import com.alemicode.amromovies.feature.movies.presentation.R
import com.alemicode.amromovies.feature.movies.domain.model.SortField
import com.alemicode.amromovies.feature.movies.domain.model.SortOrder

@Composable
fun SortControl(
    sortField: SortField,
    sortOrder: SortOrder,
    onSortFieldSelected: (SortField) -> Unit,
    onToggleSortOrder: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val orderRotation by animateFloatAsState(
        targetValue = if (sortOrder == SortOrder.DESCENDING) 0f else 180f,
        label = "sortOrderRotation",
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.space.space4),
    ) {
        Box {
            Surface(
                onClick = { isMenuExpanded = true },
                shape = AmroShapes.pill,
                color = MaterialTheme.colorScheme.surfaceVariant,
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.space.space16,
                        vertical = MaterialTheme.space.space8,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = sortField.toDisplayLabel(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            DropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = { isMenuExpanded = false },
            ) {
                SortField.entries.forEach { field ->
                    DropdownMenuItem(
                        text = { Text(field.toDisplayLabel()) },
                        onClick = {
                            onSortFieldSelected(field)
                            isMenuExpanded = false
                        },
                    )
                }
            }
        }

        IconButton(onClick = onToggleSortOrder) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                modifier = Modifier.graphicsLayer { rotationZ = orderRotation },
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SortField.toDisplayLabel(): String = stringResource(
    when (this) {
        SortField.POPULARITY -> R.string.sort_field_popularity
        SortField.TITLE -> R.string.sort_field_title
        SortField.RELEASE_DATE -> R.string.sort_field_release_date
    },
)

@ThemePreviews
@Composable
private fun SortControlPreview() {
    AmroTheme {
        SortControl(
            sortField = SortField.POPULARITY,
            sortOrder = SortOrder.DESCENDING,
            onSortFieldSelected = {},
            onToggleSortOrder = {},
        )
    }
}
