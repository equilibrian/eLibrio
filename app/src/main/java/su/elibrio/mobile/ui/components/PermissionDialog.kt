package su.elibrio.mobile.ui.components

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.elibrio.mobile.BuildConfig
import su.elibrio.mobile.R

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