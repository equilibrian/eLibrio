package su.elibrio.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.elibrio.mobile.model.Screen
import su.elibrio.mobile.ui.screens.BookScreen
import su.elibrio.mobile.ui.screens.EditScreen
import su.elibrio.mobile.ui.screens.MainScreen
import su.elibrio.mobile.ui.theme.ELibrioTheme
import su.elibrio.mobile.viewmodel.MainActivityViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ELibrioTheme {
                setupNavHost()
            }
        }
    }

    @Composable
    fun setupNavHost(
        navController: NavHostController = rememberNavController(),
        viewModel: MainActivityViewModel = hiltViewModel()
    ) {
        val ctx = LocalContext.current
        LaunchedEffect(Unit) {
            withContext(Dispatchers.IO) {
                viewModel.scanForBooks(ctx)
            }
        }

        NavHost(
            navController = navController,
            startDestination = Screen.Main.route
        ) {
            composable(Screen.Main.route) {
                MainScreen(mainNavController = navController, viewModel = viewModel)
            }
            composable(
                route = Screen.Book.route,
                arguments = listOf(navArgument("bookId") { type = NavType.IntType })
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
}