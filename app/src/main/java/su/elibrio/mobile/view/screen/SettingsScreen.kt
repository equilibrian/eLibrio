package su.elibrio.mobile.view.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import su.elibrio.mobile.ui.theme.ELibrioTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPaddings ->
        Box(modifier = modifier
            .fillMaxSize()
            .padding(innerPaddings)) {
            Text(text = "This is settings screen", modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ELibrioTheme {
        CollectionScreen()
    }
}