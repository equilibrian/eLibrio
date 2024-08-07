package su.elibrio.mobile.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import su.elibrio.mobile.R
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.ui.components.Book
import su.elibrio.mobile.ui.components.ExpandableText
import su.elibrio.mobile.ui.components.Metric
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.viewmodel.BookScreenViewModel

@Composable
fun BookDropdownMenu(
    navController: NavController,
    isMenuExpanded: MutableState<Boolean>,
    bookId: Int? = -1
) {
    val ctx = LocalContext.current
    DropdownMenu(
        expanded = isMenuExpanded.value,
        onDismissRequest = { isMenuExpanded.value = false }
    ) {
        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.st_edit)) },
            onClick = { navController.navigate("edit/${bookId}") },
        )

        DropdownMenuItem(
            text = { Text(text = stringResource(id = R.string.st_move_to_trash)) },
            onClick = {
                Toast.makeText(ctx, "Not implemented", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun BookScreenAppBarActions(navController: NavController, viewModel: BookScreenViewModel, isMenuExpanded: MutableState<Boolean>) {
    val isFavourite = viewModel.book.value?.isFavourite
    IconButton(
        onClick = { viewModel.updateFavourStatus(!(isFavourite ?: false)) }
    ) {
        AnimatedContent(targetState = isFavourite, label = "") { targetState ->
            when {
                targetState == true -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_favorite_filled),
                        contentDescription = "Back"
                    )
                }
                else -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_favorite),
                        contentDescription = "Back"
                    )
                }
            }
        }
    }

    BookDropdownMenu(navController = navController, isMenuExpanded = isMenuExpanded, bookId = viewModel.book.value?.id)
    IconButton(onClick = { isMenuExpanded.value = !isMenuExpanded.value }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.options_icon),
            contentDescription = "Options"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isTitleVisible: Boolean,
    title: String,
    viewModel: BookScreenViewModel,
    navController: NavController,
    isMenuExpanded: MutableState<Boolean>
) {
    val backgroundColor by animateColorAsState(
        if (isTitleVisible) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background,
        label = "Animated color"
    )
    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = isTitleVisible,
                enter = fadeIn(animationSpec = tween()),
                exit = fadeOut(animationSpec = tween())
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            BookScreenAppBarActions(
                navController = navController,
                viewModel = viewModel,
                isMenuExpanded = isMenuExpanded
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        )
    )
}

@Composable
fun BookCard(
    modifier: Modifier = Modifier,
    coverPageUri: Uri?,
    title: String,
    authorName: String,
    progress: Float = 0f,
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .requiredHeight(136.dp)
        .padding(horizontal = 12.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = coverPageUri,
                contentDescription = null,
                modifier = modifier.aspectRatio(0.75f),
                placeholder = painterResource(id = R.drawable.no_cover),
                error = painterResource(id = R.drawable.no_cover),
                contentScale = ContentScale.FillBounds
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = authorName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = modifier
                        .width(128.dp)
                        .padding(top = 6.dp),
                    strokeCap = StrokeCap.Round
                )
                TextButton(
                    onClick = {  },
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = modifier.height(24.dp)
                ) {
                    Text(text = stringResource(id = R.string.st_read))
                    Icon(
                        imageVector = ImageVector
                            .vectorResource(id = R.drawable.ic_expand_right_small),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun BookMetrics() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Metric(value = "0", text = "Pages")
        Metric(value = "0", text = "Chapters")
        Metric(value = "0", text = "Bookmarks")
        Metric(value = "0", text = "Quotes")
    }

    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)
}

@Composable
fun BookAnnotation(annotation: String?, isExpanded: MutableState<Boolean>) {
    ExpandableText(
        text = if (annotation.isNullOrEmpty())
            stringResource(R.string.st_no_description)
        else annotation,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .animateContentSize()
            .clickable { isExpanded.value = !isExpanded.value },
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun BooksSeries(
    books: List<Book>?,
    navController: NavController
) {
    AnimatedVisibility(visible = !books.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.st_other_books),
                    style = MaterialTheme.typography.titleSmall
                )

                TextButton(
                    onClick = {},
                    contentPadding = PaddingValues(horizontal = 0.dp),
                    modifier = Modifier.height(24.dp)
                ) {
                    Text(text = stringResource(id = R.string.st_show_all))
                }
            }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                books?.forEach { book ->
                    item { Book(navController = navController, book = book, showTitle = false) }
                }
            }
        }
    }
}

@Composable
fun BookDetails(book: Book?) {
    Column(
        modifier = Modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(id = R.string.st_about_file),
            style = MaterialTheme.typography.titleSmall
        )

        @Composable
        fun DetailItem(label: String, value: String?) {
            value?.let {
                Text(
                    text = "$label: $value",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }

        DetailItem(stringResource(R.string.st_series), book?.sequence)
        DetailItem(stringResource(R.string.st_publisher), book?.publisher)
        DetailItem(stringResource(R.string.st_year), book?.year)
        DetailItem(stringResource(R.string.st_lang), book?.lang)
        DetailItem(stringResource(R.string.st_translator), book?.translator)
        DetailItem(stringResource(R.string.st_isbn), book?.isbn)
        DetailItem(stringResource(R.string.st_format), book?.format)
        DetailItem(stringResource(R.string.st_size), book?.size)
    }
}

@Composable
fun BookScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: BookScreenViewModel = hiltViewModel(),
    bookId: Int = 0
) {
    if (bookId == -1) navController.popBackStack()
    val state = rememberLazyListState()
    val isTitleVisible by remember {
        derivedStateOf { state.firstVisibleItemIndex != 0 }
    }

    val book by viewModel.book.observeAsState(null)
    val otherBooks by viewModel.otherBooks.observeAsState(null)
    LaunchedEffect(Unit) {
        viewModel.findBook(bookId)
    }

    val isMenuExpanded = remember { mutableStateOf(false) }
    val isAnnotationExpanded = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                isTitleVisible = isTitleVisible,
                title = book?.title ?: stringResource(R.string.st_unknown),
                viewModel = viewModel,
                navController = navController,
                isMenuExpanded = isMenuExpanded
            )
        }
    ) { innerPaddings ->
        LazyColumn(
            modifier = modifier.padding(innerPaddings),
            state = state,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                BookCard(
                    coverPageUri = book?.coverPageSrc?.toUri(),
                    title = book?.title ?: stringResource(R.string.st_unknown),
                    authorName = book?.author ?: stringResource(R.string.st_unknown),
                    progress = 0f
                )
            }
            item { BookMetrics() }
            item { BookAnnotation(annotation = book?.annotation, isExpanded = isAnnotationExpanded) }
            item { BooksSeries(navController = navController, books = otherBooks) }
            item { BookDetails(book = book) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookScreenPreview() {
    ELibrioTheme {
        //BookScreen()
    }
}