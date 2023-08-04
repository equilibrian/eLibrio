package ru.equilibrian.elibrio.view.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.equilibrian.elibrio.R
import ru.equilibrian.elibrio.model.LibraryFilter
import ru.equilibrian.elibrio.model.ViewMode
import ru.equilibrian.elibrio.ui.theme.ELibrioTheme
import ru.equilibrian.elibrio.util.FAB_ANIMATION_DURATION
import ru.equilibrian.elibrio.viewmodel.LibraryScreenViewModel

val LIBRARY_FILTERS = listOf(
    LibraryFilter.Recent,
    LibraryFilter.All,
    LibraryFilter.Newest,
    LibraryFilter.Favorites,
    LibraryFilter.Unread,
    LibraryFilter.ReadLater
)

/**
 * Composable function representing the AppBar component.
 *
 * This function creates a top app bar with options to modify the view mode
 * and perform other actions.
 *
 * @param currentViewMode A [MutableState] holding the current view mode ([ViewMode.GRID]
 * or [ViewMode.LIST]) as the state that can be modified in the composition.
 * @param scrollBehavior The scrolling behavior that determines how the app bar should behave
 * when the content is scrolled.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(currentViewMode: MutableState<ViewMode>, scrollBehavior: TopAppBarScrollBehavior) {
    var isChecked by rememberSaveable { mutableStateOf(false) }
    val viewListIcon = painterResource(id = R.drawable.sort_list)
    val viewGridIcon = painterResource(id = R.drawable.dashdoard_outlined)

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "Library", color = MaterialTheme.colorScheme.onBackground)
        },
        actions = {
            Row {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.add_round),
                        contentDescription = "icon"
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "icon"
                    )
                }

                IconToggleButton(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        currentViewMode.value = if (!isChecked) ViewMode.GRID else ViewMode.LIST
                    },
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        checkedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    val icon = if (isChecked) viewGridIcon else viewListIcon
                    Icon(
                        painter = icon,
                        contentDescription = "icon"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

/**
 * Composable function representing a filter selection row.
 *
 * This function displays a row of filter options using Jetpack Compose's LazyRow.
 *
 * A list of [LibraryFilter] items to be displayed. Each filter item
 * should have a name to be shown on the chip.
 *
 * @sample LIBRARY_FILTERS
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters() {
    var selectedFilter by remember { mutableStateOf(0) }
    LazyRow(
        contentPadding = PaddingValues(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(LIBRARY_FILTERS) { idx, filter ->
            ElevatedFilterChip(
                selected = selectedFilter == idx,
                onClick = { selectedFilter = idx },
                label = { Text(text = filter.name) },
            )
        }
    }
}

@Composable
fun LibraryGridItem(title: String, poster: Painter? = null) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = true, onClick = {})
            .wrapContentSize()
            .padding(bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .defaultMinSize(minHeight = 200.dp)
        ) {
            poster?.let {
                Image(
                    painter = poster,
                    contentDescription = "poster",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryContentGridView(state: LazyStaggeredGridState, content: List<Pair<String, Painter>>?) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(128.dp),
        state = state,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content?.forEach { book ->
            item { LibraryGridItem(book.first, book.second) }
        }
        
        repeat(25) {
            item { LibraryGridItem(title = "TEST") }
        }
    }
}

@Composable
fun LibraryContentListView(state: LazyListState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Coming soon...", modifier = Modifier.align(Alignment.Center))
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(modifier: Modifier = Modifier, viewModel: LibraryScreenViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentViewMode = remember { mutableStateOf(ViewMode.GRID) }
    val contentGridState = rememberLazyStaggeredGridState()
    val contentListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val isNotAtTop = when(currentViewMode.value) {
                    ViewMode.GRID -> contentGridState.firstVisibleItemIndex != 0
                    ViewMode.LIST -> contentListState.firstVisibleItemIndex != 0
                }

                if (isNotAtTop) viewModel.showFab() else viewModel.hideFab()

                return Offset.Zero
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppBar(currentViewMode, scrollBehavior) },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(snackbarData = data)
            }
        },
        floatingActionButton = {
            val isFabVisible = viewModel::isFabVisible
            FAB(isFabVisible.get()) {
                scope.launch {
                    if (currentViewMode.value == ViewMode.GRID)
                        contentGridState.animateScrollToItem(0)
                    else contentListState.animateScrollToItem(0)
                }

                viewModel.hideFab()
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        content = { scaffoldPaddings ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPaddings)
                    .nestedScroll(nestedScrollConnection),
            ) {
                Filters()

                if (viewModel.books != null) {
                    Crossfade(targetState = currentViewMode.value, label = "") { mode ->
                        when (mode) {
                            ViewMode.GRID -> LibraryContentGridView(contentGridState, viewModel.books.value)
                            ViewMode.LIST -> LibraryContentListView(contentListState)
                        }
                    }
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        TextButton(
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Coming soon...")
                                }
                            },
                            modifier = Modifier.align(Alignment.Center),
                            contentPadding = PaddingValues(
                                horizontal = 20.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.add_round),
                                contentDescription = "icon",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Add book")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun FAB(visible: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = visible,enter = slideInVertically(
            initialOffsetY = { fullWidth -> fullWidth },
            animationSpec = spring()
        ) + fadeIn(animationSpec = tween(durationMillis = FAB_ANIMATION_DURATION)),
        exit = slideOutVertically(
            targetOffsetY = { fullWidth -> fullWidth },
            animationSpec = spring()
        ) + fadeOut(animationSpec = tween(durationMillis = FAB_ANIMATION_DURATION))
    ) {
        FloatingActionButton(
            onClick = { onClick.invoke() },
            modifier = Modifier.size(48.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(painter = painterResource(id = R.drawable.expand_up), contentDescription = "icon")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    ELibrioTheme {
        LibraryScreen()
    }
}