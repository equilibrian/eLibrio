package su.elibrio.mobile.view.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.util.LIBRARY_FILTERS
import su.elibrio.mobile.viewmodel.LibraryScreenViewModel

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

@Composable
fun CollectionView(modifier: Modifier = Modifier, files: List<Book>) {
    Column {
        files.forEach { f->
            Text(text = f.title)
        }
    }
}

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier, lsViewModel: LibraryScreenViewModel = viewModel()
) {
    val ctx = LocalContext.current

    Column(modifier = modifier
        .fillMaxSize()
        .padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FiltersView()
        if (lsViewModel.inProgress)
            LoadingView()
        else if (lsViewModel.books?.value == null || lsViewModel.books.value?.isEmpty() == true)
            EmptyCollectionView()
        else {
            CollectionView(files = mutableListOf())
            Toast.makeText(ctx, "Показывать книги пока не умеем", Toast.LENGTH_SHORT).show()
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