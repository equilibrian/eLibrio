package su.elibrio.mobile.view.screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.util.LIBRARY_FILTERS
import su.elibrio.mobile.viewmodel.LibraryScreenViewModel
import timber.log.Timber

@Composable
fun FiltersView() {
    var selectedFilter by remember { mutableIntStateOf(0) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LIBRARY_FILTERS.forEachIndexed { idx, filter ->
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

fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (ex: IllegalArgumentException) {
        Timber.tag("LibraryScreen").e(ex)
        null
    }
}

@Composable
fun BookView(modifier: Modifier = Modifier, book: Book) {
    var image by remember { mutableStateOf<Bitmap?>(null) }
    var title by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(book) {
        if (book is Book.FictionBook) {
            val imgId = book.description.titleInfo.coverPage?.image?.imageId
            var base64: String? = null
            book.binaries?.forEach { binary ->
                if (binary.id == imgId) base64 = binary.value
            }
            image = base64?.let { base64ToBitmap(it) }

            title = book.description.titleInfo.bookTitle
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        image?.let {
            Card(
                shape = RoundedCornerShape(2.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .aspectRatio(0.75f)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        title?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
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
    modifier: Modifier = Modifier, lsViewModel: LibraryScreenViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val inProgress by lsViewModel.inProgress.observeAsState(false)
    val isFiltersVisible by lsViewModel.isFiltersVisible.observeAsState(true)
    val books by lsViewModel.books.observeAsState(emptyList())

    val contentGridState = rememberLazyGridState()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) lsViewModel.hideFilters()
                else if (available.y > 1) lsViewModel.showFilters()

                return Offset.Zero
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            lsViewModel.scanForBooks(ctx)
        } catch (ex: Exception) {
            Timber.tag("LibraryScreen").e(ex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding()
            .nestedScroll(nestedScrollConnection),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isFiltersVisible,
        ) {
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