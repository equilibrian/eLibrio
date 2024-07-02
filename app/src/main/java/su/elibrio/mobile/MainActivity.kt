package su.elibrio.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import su.elibrio.mobile.model.Screen
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.view.screen.AuthorsScreen
import su.elibrio.mobile.view.screen.CollectionScreen
import su.elibrio.mobile.view.screen.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ELibrioTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(modifier = modifier.fillMaxSize(),
        content =  { innerPaddings ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)) {
                NavHost(navController = navController, startDestination = Screen.Collection.route) {
                    composable(Screen.Collection.route) { CollectionScreen() }
                    composable(Screen.Authors.route) { AuthorsScreen() }
                    composable(Screen.Settings.route) { SettingsScreen() }
                }
            }
        },
        bottomBar = { BottomNavigation(navController = navController) }
    )
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf(Screen.Collection, Screen.Authors, Screen.Settings)

    NavigationBar {
        items.forEachIndexed { idx, item ->
            NavigationBarItem(
                selected = selectedItem == idx,
                onClick = {
                    selectedItem = idx
                    navController.navigate(item.route)
                },
                label = { Text(stringResource(id = item.titleStrId)) },
                icon = { Icon(
                    painter = painterResource(item.icon),
                    contentDescription = stringResource(id = item.titleStrId)
                ) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ELibrioTheme {
        MainScreen()
    }
}