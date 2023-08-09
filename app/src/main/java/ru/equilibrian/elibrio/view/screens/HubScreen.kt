package ru.equilibrian.elibrio.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.equilibrian.elibrio.BuildConfig
import ru.equilibrian.elibrio.R
import ru.equilibrian.elibrio.ui.theme.ELibrioTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.Transparent),
        title = {
            Text(text = "Hub", color = MaterialTheme.colorScheme.onBackground)
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.setting_line),
                    contentDescription = "icon"
                )
            }
        }
    )
}

@Composable
fun Statistics() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Most productive day: None")
            Icon(
                painter = painterResource(id = R.drawable.expand_right),
                contentDescription = "icon"
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Coming soon...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun Notes() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Notes")
            Icon(
                painter = painterResource(id = R.drawable.expand_right),
                contentDescription = "icon"
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Coming soon...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun Backups() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Backups")
            Icon(
                painter = painterResource(id = R.drawable.expand_right),
                contentDescription = "icon"
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(148.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Coming soon...",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar() },
        content = { scaffoldPaddings ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddings)
                .padding(horizontal = 16.dp)
            ) {
                Statistics()
                Notes()
                Backups()

                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    val version = BuildConfig.VERSION_NAME
                    Text(
                        text = "eLibrio v.$version",
                        modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HubScreenPreview() {
    ELibrioTheme {
        HubScreen()
    }
}