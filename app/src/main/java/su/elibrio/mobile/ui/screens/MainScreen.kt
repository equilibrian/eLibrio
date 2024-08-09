package su.elibrio.mobile.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Screen
import su.elibrio.mobile.ui.behavior.MainScrollConnection
import su.elibrio.mobile.ui.components.BottomNavigation
import su.elibrio.mobile.ui.components.CheckPermissionsAndShowDialog
import su.elibrio.mobile.viewmodel.MainActivityViewModel

@Composable
fun AppBarActions(currentScreen: Screen) {
    Row {
        if (currentScreen != Screen.Settings) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.search_icon),
                    contentDescription = "icon"
                )
            }
        }

        if(currentScreen != Screen.Authors
            && currentScreen != Screen.Settings
        ) {
            IconButton(onClick = {
            }) {
                Icon(painter = painterResource(R.drawable.add_icon), contentDescription = null)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    currentScreen: Screen
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        title = {
            Text(
                text = stringResource(id = currentScreen.titleStrId),
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        actions = { AppBarActions(currentScreen) },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainNavController: NavController,
    viewModel: MainActivityViewModel
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val externalStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = produceState<Screen>(initialValue = Screen.Library, key1 = navBackStackEntry) {
        value = Screen.getDestinationByRoute(navBackStackEntry?.destination?.route)
    }

    val nestedScrollConnection = remember {
        MainScrollConnection(scrollBehavior = scrollBehavior, viewModel = viewModel)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        topBar = { AppBar(scrollBehavior, currentScreen.value) },
        content = { innerPaddings ->
            NavHost(
                navController = navController,
                startDestination = Screen.Library.route,
                modifier = Modifier.padding(innerPaddings)
            ) {
                composable(Screen.Library.route) {
                    LibraryScreen(
                        navController = mainNavController,
                        viewModel = viewModel
                    )
                }
                composable(Screen.Authors.route) { AuthorsScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
            }
        },
        bottomBar = { BottomNavigation(navController = navController) }
    )

    CheckPermissionsAndShowDialog(openAlertDialog, externalStoragePermissionState)
}
