package ru.equilibrian.elibrio

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.equilibrian.elibrio.ui.theme.ELibrioTheme
import kotlin.math.roundToInt

const val TOP_BAR_HEIGHT = 56
const val BOTTOM_PADDING = 56

sealed class Filter(val name: String, val filter: String) {
    object Recent : Filter("Recent", "recent")
    object All : Filter("All", "all")
    object Newest : Filter("Newest", "newest")
    object Favorites : Filter("Favorites", "favorites")
    object Unread : Filter("Unread", "unread")
    object ReadLater : Filter("Read later", "read_later")
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(intOffset: IntOffset) {
    var checkedState by remember { mutableStateOf(false) }
    val uncheckedIcon = painterResource(id = R.drawable.sort_list)
    val checkedIcon = painterResource(id = R.drawable.dashdoard_outlined)

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Text(text = "Library", color = MaterialTheme.colorScheme.onBackground)
        },
        modifier = Modifier.offset { intOffset },
        actions = {
            Row(modifier = Modifier.padding(end = 16.dp)) {
                IconButton(onClick = {

                }) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "search"
                    )
                }

                IconToggleButton(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it },
                    colors = IconButtonDefaults.iconToggleButtonColors(
                        checkedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    val icon = if (checkedState) checkedIcon else uncheckedIcon
                    Icon(
                        painter = icon,
                        contentDescription = "icon"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters() {
    val listFilters = listOf(Filter.Recent, Filter.All, Filter.Newest, Filter.Favorites, Filter.Unread, Filter.ReadLater)
    var selectedFilter by remember { mutableStateOf(0) }
    LazyRow(contentPadding = PaddingValues(start = 16.dp)) {
        itemsIndexed(listFilters) { idx, filter ->
            ElevatedFilterChip(
                modifier = Modifier.padding(end = 8.dp),
                selected = selectedFilter == idx,
                onClick = {
                    selectedFilter = idx
                },
                label = { Text(text = filter.name) },
            )
        }
    }
}

val onAddBookClick: () -> Unit = {

}

@Composable
fun LibraryItem() {
    Card(
        modifier = Modifier
            .heightIn(200.dp, 272.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = true, onClick = onAddBookClick)
            .padding(bottom = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = R.drawable.add_round),
                contentDescription = "icon",
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryContentView() {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        columns = StaggeredGridCells.Adaptive(128.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(25) {
            item { LibraryItem() }
        }
    }
}

@Composable
fun LibraryScreen() {
    val appBarHeightPx = with(LocalDensity.current) { TOP_BAR_HEIGHT.dp.roundToPx().toFloat() }
    val appBarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = appBarOffsetHeightPx.value + delta
                appBarOffsetHeightPx.value = newOffset.coerceIn(-appBarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    val animatedAppBarOffset by animateIntOffsetAsState(
        targetValue = IntOffset(x = 0, y = appBarOffsetHeightPx.value.roundToInt())
    )
    AppBar(animatedAppBarOffset)

    val animatedContentHeight by animateDpAsState(
        targetValue = maxOf(TOP_BAR_HEIGHT.dp + (appBarOffsetHeightPx.value.roundToInt().dp / 3), 0.dp)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = animatedContentHeight, bottom = BOTTOM_PADDING.dp)
            .nestedScroll(nestedScrollConnection),
    ) {
        Filters()
        LibraryContentView()
    }
}

@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    ELibrioTheme {
        LibraryScreen()
    }
}