package ru.equilibrian.elibrio.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.equilibrian.elibrio.R
import ru.equilibrian.elibrio.model.Screen
import ru.equilibrian.elibrio.ui.theme.ELibrioTheme
import ru.equilibrian.elibrio.util.FAB_ANIMATION_DURATION
import ru.equilibrian.elibrio.view.screens.AuthorsScreen
import ru.equilibrian.elibrio.view.screens.HubScreen
import ru.equilibrian.elibrio.view.screens.LibraryScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ELibrioTheme {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        content = { scaffoldPaddings ->
            Box(modifier = Modifier.fillMaxSize().padding(scaffoldPaddings)) {
                NavHost(navController = navController, startDestination = Screen.Library.route) {
                    composable(Screen.Library.route) { LibraryScreen() }
                    composable(Screen.Hub.route) { HubScreen() }
                    composable(Screen.Authors.route) { AuthorsScreen() }
                }
            }
        },
        bottomBar = { BottomNavigation(navController) },
    )
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Screen.Library, Screen.Hub, Screen.Authors)

    NavigationBar {
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ELibrioTheme {
        MainScreen()
    }
}