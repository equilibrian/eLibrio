package su.elibrio.mobile.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.elibrio.mobile.R
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.ui.components.Book
import su.elibrio.mobile.ui.components.LibraryFilters
import su.elibrio.mobile.ui.components.Loading
import su.elibrio.mobile.viewmodel.MainActivityViewModel

@Composable
fun Collection(
    modifier: Modifier = Modifier,
    state: LazyGridState,
    books: List<Book>?,
    navController: NavController
) {
    if (!books.isNullOrEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 114.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            state = state,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(books.size) { idx ->
                Book(book = books[idx], navController = navController)
            }
        }
    } else {
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
}

@Composable
fun LibraryScreen(
    viewModel: MainActivityViewModel,
    navController: NavController,
) {
    val contentGridState = rememberLazyGridState()
    val inProgress by viewModel.inProgress.observeAsState(false)
    val isFiltersVisible by viewModel.isFiltersVisible.observeAsState(true)
    val books: List<Book>? by viewModel.books.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = isFiltersVisible) { LibraryFilters() }

        AnimatedContent(
            targetState = inProgress,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(2000)
                ) togetherWith fadeOut(animationSpec = tween(2000))
            },
            label = ""
        ) { targetState ->
            when {
                targetState -> Loading()
                else -> Collection(books = books, state = contentGridState, navController = navController)
            }
        }
    }
}