package su.elibrio.mobile.view.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import su.elibrio.mobile.BuildConfig
import su.elibrio.mobile.R
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.view.activities.LicensesActivity
import su.elibrio.mobile.view.components.SettingsButton

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.st_category_general),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelLarge
        )

        Card(modifier = modifier.fillMaxWidth()) {
            SettingsButton(text = stringResource(id = R.string.st_scanning_files), onClick = {})
            HorizontalDivider(modifier = modifier.padding(horizontal = 12.dp))
            SettingsButton(text = stringResource(id = R.string.st_backup), onClick = {})
        }

        Text(
            text = stringResource(id = R.string.st_other),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelLarge
        )

        Card(modifier = modifier.fillMaxWidth()) {
            SettingsButton(text = stringResource(id = R.string.st_licenses), onClick = {
                val intent = Intent(ctx, LicensesActivity::class.java)
                startActivity(ctx, intent, null)
            })
        }

        TextButton(
            onClick = {  },
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = "${stringResource(id = R.string.st_elibrio_version)}${BuildConfig.VERSION_NAME}",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ELibrioTheme {
        SettingsScreen()
    }
}