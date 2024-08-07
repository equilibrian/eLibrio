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
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.elibrio.mobile.model.Screen
import su.elibrio.mobile.ui.screens.AuthorsScreen
import su.elibrio.mobile.ui.screens.BookScreen
import su.elibrio.mobile.ui.screens.EditScreen
import su.elibrio.mobile.ui.screens.LibraryScreen
import su.elibrio.mobile.ui.screens.SettingsScreen
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.utils.BOTTOM_NAVIGATION_DESTINATIONS
import su.elibrio.mobile.viewmodel.MainActivityViewModel
import timber.log.Timber

@AndroidEntryPoint
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
fun AppBarActions(navController: NavController) {
    val currentDestination = navController.currentDestination?.route
    Row {
        if (currentDestination != Screen.Settings.route) {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.search_icon),
                    contentDescription = "icon"
                )
            }
        }

        if(currentDestination != Screen.Authors.route
            && currentDestination != Screen.Settings.route
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
    navController: NavController
) {
    val currentDestination =
        Screen.getDestinationByRoute(navController.currentDestination?.route)
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        title = {
            Text(
                text = stringResource(id = currentDestination.titleStrId),
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        actions = { AppBarActions(navController = navController) },
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
        val needsPermission = withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                !Environment.isExternalStorageManager()
            else
                !externalStoragePermissionState.status.isGranted
        }
        withContext(Dispatchers.Main) {
            openAlertDialog.value = needsPermission
        }
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
    viewModel: MainActivityViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route
    ) {
        composable(Screen.Library.route) {
            LibraryScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Authors.route) {
            AuthorsScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        composable(
            route = Screen.Book.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType }),
            enterTransition = { slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) }
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId")
            if (bookId != null) {
                BookScreen(navController = navController, bookId = bookId)
            }
        }
        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId")
            if (bookId != null) {
                EditScreen(navController = navController, bookId = bookId)
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    NavigationBar {
        BOTTOM_NAVIGATION_DESTINATIONS.forEachIndexed { idx, screen ->
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
                    screen.icon?.let { resource -> painterResource(resource) }?.let { painter ->
                        Icon(
                            painter = painter,
                            contentDescription = stringResource(id = screen.titleStrId)
                        )
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }
    val externalStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isNavigationVisible = rememberSaveable { (mutableStateOf(true)) }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (Screen.getDestinationByRoute(navBackStackEntry?.destination?.route) in BOTTOM_NAVIGATION_DESTINATIONS) {
        true -> isNavigationVisible.value = true
        false -> {
            isNavigationVisible.value = false
            Timber.d("TRUE: ${navController.currentDestination?.route}")
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val scrollBehaviorOffset = scrollBehavior.nestedScrollConnection.onPreScroll(available, source)

                val isTopBarCollapsed = scrollBehavior.state.collapsedFraction == 1f

                if (available.y < -24 && viewModel.isFiltersVisible.value == true && isTopBarCollapsed)
                    viewModel.hideFilters()
                else if (available.y > 1 && viewModel.isFiltersVisible.value == false)
                    viewModel.showFilters()

                return scrollBehaviorOffset
            }
        }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            viewModel.scanForBooks(ctx)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        topBar = { if (isNavigationVisible.value) { AppBar(scrollBehavior, navController) } },
        content = { innerPaddings ->
            val paddings = if (isNavigationVisible.value) PaddingValues(
                top = innerPaddings.calculateTopPadding(),
                bottom = innerPaddings.calculateBottomPadding(),
            ) else PaddingValues(0.dp)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                setupNavHost(navController, viewModel)
            }
        },
        bottomBar = {
            if (isNavigationVisible.value) {
                BottomNavigation(navController = navController)
            }
        }
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