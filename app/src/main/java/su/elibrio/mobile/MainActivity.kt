package su.elibrio.mobile

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import su.elibrio.mobile.model.MainActivityScreens
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.utils.MAIN_ACTIVITY_SCREENS
import su.elibrio.mobile.ui.screens.AuthorsScreen
import su.elibrio.mobile.ui.screens.LibraryScreen
import su.elibrio.mobile.ui.screens.SettingsScreen

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
fun AppBarActions(currentScreen: MainActivityScreens) {
    val ctx = LocalContext.current
    Row {
        AnimatedVisibility(visible = currentScreen != MainActivityScreens.Settings) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.search_icon),
                    contentDescription = "icon"
                )
            }
        }

        AnimatedVisibility(
            visible = currentScreen != MainActivityScreens.Authors
                    && currentScreen != MainActivityScreens.Settings
        ) {
            IconButton(onClick = {
                val intent = Intent(ctx, BookActivity::class.java)
                startActivity(ctx, intent, null)
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
    currentScreenState: MutableState<MainActivityScreens>
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        title = {
            Crossfade(targetState = currentScreenState.value.titleStrId, label = "") { titleId ->
                Text(
                    text = stringResource(id = titleId),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        actions = { AppBarActions(currentScreen = currentScreenState.value) },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckPermissionsAndShowDialog(
    openAlertDialog: MutableState<Boolean>,
    externalStoragePermissionState: PermissionState
) {
    val ctx = LocalContext.current
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            openAlertDialog.value = !Environment.isExternalStorageManager()
        else
            openAlertDialog.value = !externalStoragePermissionState.status.isGranted
    }

    if (openAlertDialog.value) {
        AlertDialog(
            title = { Text(text = stringResource(R.string.st_dialog_storage_access_needed_title)) },
            text = { Text(text = stringResource(R.string.st_dialog_storage_access_needed_body)) },
            onDismissRequest = { openAlertDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    openAlertDialog.value = false
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                        val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")
                        val intent = Intent(
                            Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                            uri
                        )
                        startActivity(ctx, intent, null)
                    } else {
                        externalStoragePermissionState.launchPermissionRequest()
                    }
                }) { Text(text = stringResource(R.string.st_continue)) }
            },
            dismissButton = {
                TextButton(onClick = { openAlertDialog.value = false }) {
                    Text(text = stringResource(R.string.st_cancel))
                }
            }
        )
    }
}

@Composable
fun setupNavHost(
    navController: NavHostController,
    currentScreenState: MutableState<MainActivityScreens>
) {
    NavHost(
        navController = navController,
        startDestination = MainActivityScreens.Library.route
    ) {
        composable(MainActivityScreens.Library.route) {
            LibraryScreen()
            currentScreenState.value = MainActivityScreens.Library
        }
        composable(MainActivityScreens.Authors.route) {
            AuthorsScreen()
            currentScreenState.value = MainActivityScreens.Authors
        }
        composable(MainActivityScreens.Settings.route) {
            SettingsScreen()
            currentScreenState.value = MainActivityScreens.Settings
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        MAIN_ACTIVITY_SCREENS.forEachIndexed { idx, screen ->
            NavigationBarItem(
                selected = selectedItem == idx,
                onClick = {
                    if (selectedItem != idx) {
                        selectedItem = idx
                        navController.navigate(screen.route)
                    }
                },
                label = { Text(text = stringResource(id = screen.titleStrId)) },
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = stringResource(id = screen.titleStrId)
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val externalStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val navController = rememberNavController()
    val currentScreenState = remember {
        mutableStateOf<MainActivityScreens>(MainActivityScreens.Library)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { AppBar(scrollBehavior, currentScreenState) },
        content = { innerPaddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = innerPaddings.calculateTopPadding(),
                        bottom = innerPaddings.calculateBottomPadding()
                    )
            ) {
                setupNavHost(navController, currentScreenState)
            }
        },
        bottomBar = { BottomNavigation(navController = navController) }
    )

    CheckPermissionsAndShowDialog(openAlertDialog, externalStoragePermissionState)
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ELibrioTheme {
        MainScreen()
    }
}