package su.elibrio.mobile.model

import su.elibrio.mobile.R

sealed class Screen(val route: String, val titleStrId: Int, val icon: Int?) {
    data object Main : Screen("main", R.string.app_name, R.drawable.collection_icon)
    data object Library : Screen("library", R.string.library_screen_title, R.drawable.collection_icon)
    data object Authors : Screen("authors", R.string.authors_screen_title, R.drawable.authors_icon)
    data object Settings : Screen("settings", R.string.settings_screen_title, R.drawable.setting_fill)
    data object Book : Screen("book/{bookId}", R.string.st_book_screen_title, null)
    data object Edit : Screen("edit/{bookId}", R.string.st_title_edit, null)

    companion object {
        fun getDestinationByRoute(route: String?): Screen {
            return when (route) {
                Main.route -> Main
                Library.route -> Library
                Authors.route -> Authors
                Settings.route -> Settings
                Book.route -> Book
                Edit.route -> Edit
                else -> Library
            }
        }
    }
}