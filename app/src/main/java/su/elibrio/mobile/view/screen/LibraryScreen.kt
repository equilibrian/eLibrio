package su.elibrio.mobile.view.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.model.LibraryFilter
import su.elibrio.mobile.model.MainActivityScreens
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.viewmodel.LibraryScreenViewModel

val LIBRARY_FILTERS = listOf(
    LibraryFilter.All,
    LibraryFilter.Recent,
    LibraryFilter.Favourites
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(scrollBehavior: TopAppBarScrollBehavior) {
    val ctx = LocalContext.current

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(
                text = stringResource(id = MainActivityScreens.Library.titleStrId),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        actions = {
            Row {
                IconButton(onClick = {
                    Toast.makeText(ctx, "Поиск пока не работает", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.search_icon),
                        contentDescription = "icon"
                    )
                }

                IconButton(onClick = {
                    Toast.makeText(ctx, "Импорт пока не работает", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.add_icon),
                        contentDescription = "icon"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun FiltersView() {
    var selectedFilter by remember { mutableIntStateOf(0) }
    LazyRow(
        contentPadding = PaddingValues(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(LIBRARY_FILTERS) { idx, filter ->
            FilterChip(
                selected = selectedFilter == idx,
                onClick = { selectedFilter = idx },
                label = { Text(text = stringResource(id = filter.title)) },
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedFilter == idx,
                    borderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        LinearProgressIndicator(
            modifier = modifier
                .align(Alignment.Center)
                .width(100.dp),
            strokeCap = StrokeCap.Round
        )
    }
}

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
fun CollectionView(modifier: Modifier = Modifier, files: List<Book>) {
    Column {
        files.forEach { f->
            Text(text = f.title)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    modifier: Modifier = Modifier, lsViewModel: LibraryScreenViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = { AppBar(scrollBehavior) },
        content = { innerPaddings ->
            Column(modifier = modifier
                .fillMaxSize()
                .padding(top = innerPaddings.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FiltersView()
                if (lsViewModel.inProgress)
                    LoadingView()
                else if (lsViewModel.books == null || lsViewModel.books.value?.isEmpty() == true)
                    EmptyCollectionView()
                else {
                    CollectionView(files = mutableListOf())
                    Toast.makeText(ctx, "Показывать книги пока не умеем", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CollectionScreenPreview() {
    ELibrioTheme {
        CollectionScreen()
    }
}