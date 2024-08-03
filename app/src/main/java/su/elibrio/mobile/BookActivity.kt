package su.elibrio.mobile

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mikepenz.aboutlibraries.ui.compose.m3.HtmlText
import dagger.hilt.android.AndroidEntryPoint
import su.elibrio.mobile.ui.components.MetricView
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.viewmodel.BookActivityViewModel

@AndroidEntryPoint
class BookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bookId = intent.getIntExtra("BOOK_ID", -1)

        enableEdgeToEdge()
        setContent {
            ELibrioTheme {
                BookScreen(bookId = bookId)
            }
        }
    }
}

@Composable
fun TopBarActions() {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_favorite),
            contentDescription = "Back"
        )
    }

    IconButton(onClick = { /*TODO*/ }) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.options_icon),
            contentDescription = "Back"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(isTitleVisible: Boolean) {
    val ctx = LocalContext.current
    TopAppBar(
        title = {
            AnimatedVisibility(
                visible = isTitleVisible,
                enter = fadeIn(animationSpec = tween()),
                exit = fadeOut(animationSpec = tween())
            ) {
                Text(
                    text = "Book title",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                val activity = ctx as Activity
                activity.finish()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = { TopBarActions() },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
    )
}

@Composable
fun BookCard(
    coverPage: Bitmap,
    title: String,
    authorName: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                bitmap = coverPage.asImageBitmap(),
                contentDescription = null,
                modifier = modifier.width(85.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(text = title)
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
                    Text(text = "Continue")
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
            .padding(6.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        MetricView(value = "317", text = "Pages")
        MetricView(value = "18", text = "Chapters")
        MetricView(value = "7", text = "Bookmarks")
        MetricView(value = "12", text = "Quotes")
    }

    HorizontalDivider(thickness = 0.5.dp)
}

@Composable
fun BookDescription(description: String?) {
    HtmlText(
        modifier = Modifier.padding(top = 12.dp),
        html = if (description.isNullOrEmpty()) "No description" else description
    )
    /*Text(
        modifier = Modifier.padding(top = 12.dp),
        text = if (description.isNullOrEmpty()) "No description" else description,
        style = MaterialTheme.typography.bodyMedium
    )*/
}

@Composable
fun BookScreen(
    modifier: Modifier = Modifier,
    viewModel: BookActivityViewModel = hiltViewModel(),
    bookId: Int = 0
) {
    val ctx = LocalContext.current
    if (bookId == -1) (ctx as Activity).finish()
    val state = rememberLazyListState()
    val isTitleVisible by remember {
        derivedStateOf { state.firstVisibleItemIndex != 0 }
    }

    val book by viewModel.book.observeAsState(null)
    LaunchedEffect(bookId) {
        viewModel.findBook(bookId)
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopBar(isTitleVisible) }
    ) { innerPaddings ->
        LazyColumn(
            modifier = modifier
                .padding(innerPaddings)
                .padding(horizontal = 12.dp),
            state = state
        ) {
            item {
                if (book != null) {
                    BookCard(
                        coverPage = book!!.getCoverPage(ctx),
                        title = book!!.getBookTitle(),
                        authorName = book!!.getAuthorFullName() ?: "Unknown",
                        progress = 0f
                    )
                }
            }
            item { BookMetrics() }
            item { BookDescription(book?.getBookDescription()) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ELibrioTheme {
        BookScreen()
    }
}