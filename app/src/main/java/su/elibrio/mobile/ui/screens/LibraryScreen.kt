package su.elibrio.mobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.ui.components.BookView
import su.elibrio.mobile.ui.components.FiltersView
import su.elibrio.mobile.ui.components.LoadingView
import su.elibrio.mobile.viewmodel.MainActivityViewModel

@Composable
fun EmptyCollectionView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.book_open_icon),
                contentDescription = "book",
                modifier = modifier.align(Alignment.CenterHorizontally),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.outlineVariant)
            )

            Text(
                text = stringResource(id = R.string.nothing_to_show),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun CollectionView(modifier: Modifier = Modifier, state: LazyGridState, files: List<Book>?) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 114.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        state = state,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        files?.forEach { book ->
            item { BookView(book = book) }
        }
    }
}

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier, viewModel: MainActivityViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val inProgress by viewModel.inProgress.observeAsState(false)
    val isFiltersVisible by viewModel.isFiltersVisible.observeAsState(true)
    val books by viewModel.books.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.scanForBooks(ctx)
    }

    val contentGridState = rememberLazyGridState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) viewModel.hideFilters()
                else if (available.y > 1) viewModel.showFilters()

                return Offset.Zero
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding()
            .nestedScroll(nestedScrollConnection),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = isFiltersVisible) {
            FiltersView()
        }

        when {
            inProgress -> LoadingView()
            books.isEmpty() -> EmptyCollectionView()
            else -> CollectionView(files = books, state = contentGridState)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CollectionScreenPreview() {
    ELibrioTheme {
        LibraryScreen()
    }
}