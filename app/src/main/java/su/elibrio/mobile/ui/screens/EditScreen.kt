package su.elibrio.mobile.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import su.elibrio.mobile.R
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.ui.components.Loading
import su.elibrio.mobile.viewmodel.BookScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.st_title_edit)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
fun InputField(value: MutableState<String>, label: String, singleLine: Boolean = true) {
    TextField(
        value = value.value,
        onValueChange = { text -> value.value = text},
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        label = { Text(text = label) },
        maxLines = if (singleLine) 1 else 12,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun EditForm(
    navController: NavController,
    viewModel: BookScreenViewModel,
    paddingValues: PaddingValues,
    book: Book
) {
    val ctx = LocalContext.current
    val toastMessage = stringResource(id = R.string.st_changes_saved)

    val title = remember { mutableStateOf(book.title) }
    val author = remember { mutableStateOf(book.author ?: "") }
    val sequence = remember { mutableStateOf(book.sequence ?: "") }
    val annotation = remember { mutableStateOf(book.annotation ?: "") }
    val newBook = book.copy(
        title = title.value,
        author = author.value,
        sequence = sequence.value,
        annotation = annotation.value
    )

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = book.coverPageSrc,
                contentDescription = "Book cover page",
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        Toast
                            .makeText(
                                ctx,
                                "Changing the cover page is not implemented",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    },
                placeholder = painterResource(id = R.drawable.no_cover),
                error = painterResource(id = R.drawable.no_cover),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                InputField(value = title, label = stringResource(id = R.string.st_title))
                InputField(value = author, label = stringResource(id = R.string.st_author))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    InputField(value = sequence, label = stringResource(id = R.string.st_sequence))
                }
            }
        }

        InputField(value = annotation, label = stringResource(id = R.string.st_annotation), singleLine = false)

        Button(
            onClick = {
                viewModel.updateBook(newBook)
                Toast.makeText(ctx, toastMessage, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = book != newBook
        ) {
            Text(text = stringResource(id = R.string.st_save_changes))
        }
    }
}

@Composable
fun EditScreen(
    navController: NavController,
    viewModel: BookScreenViewModel = hiltViewModel(),
    bookId: Int = 0
) {
    if (bookId == -1) navController.popBackStack()

    val book by viewModel.book.observeAsState(null)

    LaunchedEffect(Unit) {
        viewModel.findBook(bookId)
    }

    Scaffold(
        topBar = { TopBar(navController = navController) }
    ) { innerPaddings ->
        AnimatedContent(targetState = book != null, label = "") { targetState ->
            when {
                targetState -> book?.let {
                    EditForm(
                        navController = navController,
                        viewModel = viewModel,
                        paddingValues = innerPaddings,
                        book = it
                    )
                }
                else -> Loading()
            }
        }
    }
}