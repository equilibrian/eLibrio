package su.elibrio.mobile.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import su.elibrio.mobile.utils.BOTTOM_NAVIGATION_DESTINATIONS

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