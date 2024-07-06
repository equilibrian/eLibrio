package su.elibrio.mobile.view.screen

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
import su.elibrio.mobile.AboutAppActivity
import su.elibrio.mobile.R
import su.elibrio.mobile.ui.theme.ELibrioTheme

@Composable
fun SettingsButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_expand_right),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

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
            SettingsButton(text = stringResource(id = R.string.st_about_app), onClick = {
                val intent = Intent(ctx, AboutAppActivity::class.java)
                startActivity(ctx, intent, null)
            })
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