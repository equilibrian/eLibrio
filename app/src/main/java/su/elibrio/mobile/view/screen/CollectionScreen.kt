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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Screen
import su.elibrio.mobile.ui.theme.ELibrioTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(scrollBehavior: TopAppBarScrollBehavior) {
    val ctx = LocalContext.current
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(
                text = stringResource(id = Screen.Collection.titleStrId),
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

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.st_import)) },
                        onClick = { Toast.makeText(ctx, "Импорт пока не работает", Toast.LENGTH_SHORT).show() },
                    )
                }

                IconButton(onClick = {
                    menuExpanded = true
                }) {
                    Icon(
                        painter = painterResource(R.drawable.options_icon),
                        contentDescription = "icon"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        LinearProgressIndicator(
            progress = { 0.43f },
            modifier = modifier
                .align(Alignment.Center)
                .width(100.dp)
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
            )

            Text(
                text = stringResource(id = R.string.nothing_to_show),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = { AppBar(scrollBehavior) },
        content =  { innerPaddings ->
            Box(modifier = modifier
                .fillMaxSize()
                .padding(innerPaddings)) {
                //LoadingView()
                EmptyCollectionView()
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