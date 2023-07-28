package ru.equilibrian.elibrio.view.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.equilibrian.elibrio.ui.theme.ELibrioTheme

@Composable
fun AuthorsScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Authors screen in development...", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorsScreenPreview() {
    ELibrioTheme {
        AuthorsScreen()
    }
}