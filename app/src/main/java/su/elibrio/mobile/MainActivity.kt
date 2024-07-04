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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import su.elibrio.mobile.model.MainActivityScreens
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

val MAIN_ACTIVITY_SCREENS = listOf(
    MainActivityScreens.Library,
    MainActivityScreens.Authors,
    MainActivityScreens.Settings
)

@Composable
fun AlertDialogPermissionsNeeded(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = stringResource(R.string.st_dialog_storage_access_needed_title)) },
        text = { Text(text = stringResource(R.string.st_dialog_storage_access_needed_body)) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(text = stringResource(R.string.st_continue))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(R.string.st_cancel))
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val externalStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    val ctx = LocalContext.current
    val navController = rememberNavController()

    Scaffold(modifier = modifier.fillMaxSize(),
        content =  { innerPaddings ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPaddings.calculateBottomPadding())) {
                NavHost(
                    navController = navController,
                    startDestination = MainActivityScreens.Library.route
                ) {
                    composable(MainActivityScreens.Library.route) { CollectionScreen() }
                    composable(MainActivityScreens.Authors.route) { AuthorsScreen() }
                    composable(MainActivityScreens.Settings.route) { SettingsScreen() }
                }
            }
        },
        bottomBar = { BottomNavigation(navController = navController) }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            openAlertDialog.value = !Environment.isExternalStorageManager()
        else
            openAlertDialog.value = !externalStoragePermissionState.status.isGranted
    }

    if (openAlertDialog.value) {
        AlertDialogPermissionsNeeded(
            onDismissRequest = { openAlertDialog.value = false },
            onConfirmation = {
                openAlertDialog.value = false
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    val uri = Uri.parse("package:su.elibrio.mobile")
                    val intent = Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                        uri
                    )

                    startActivity(ctx, intent, null)
                } else {
                    externalStoragePermissionState.launchPermissionRequest()
                }
            }
        )
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
                    selectedItem = idx
                    navController.navigate(screen.route)
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

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ELibrioTheme {
        MainScreen()
    }
}