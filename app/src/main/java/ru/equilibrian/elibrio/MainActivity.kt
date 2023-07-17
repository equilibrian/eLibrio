package ru.equilibrian.elibrio

import android.os.Bundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.equilibrian.elibrio.ui.theme.ELibrioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ELibrioTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Library : Screen("library", "Library", R.drawable.book_fill)
    object Hub : Screen("hub", "Hub", R.drawable.darhboard_fill)
    object Authors : Screen("authors", "Authors", R.drawable.authors_fill)
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        var selectedItem by remember { mutableStateOf(0) }
        val items = listOf(Screen.Library, Screen.Hub, Screen.Authors)
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.Library.route) {
            composable(Screen.Library.route) { LibraryScreen() }
            composable(Screen.Hub.route) { HubScreen() }
            composable(Screen.Authors.route) { AuthorsScreen() }
        }

        NavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            items.forEachIndexed { idx, item ->
                NavigationBarItem(
                    selected = selectedItem == idx,
                    onClick = {
                        selectedItem = idx
                        navController.navigate(item.route)
                    },
                    label = { Text(item.title) },
                    icon = { Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title
                    ) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ELibrioTheme {
        MainScreen()
    }
}