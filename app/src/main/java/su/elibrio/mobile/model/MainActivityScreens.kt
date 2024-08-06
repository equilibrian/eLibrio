package su.elibrio.mobile.model

import su.elibrio.mobile.R

sealed class MainActivityScreens(val route: String, val titleStrId: Int, val icon: Int?) {
    data object Library : MainActivityScreens("library", R.string.library_screen_title, R.drawable.collection_icon)
    data object Authors : MainActivityScreens("authors", R.string.authors_screen_title, R.drawable.authors_icon)
    data object Settings : MainActivityScreens("settings", R.string.settings_screen_title, R.drawable.setting_fill)
    data object Book : MainActivityScreens("book/{bookId}", R.string.st_book_screen_title, null)

    companion object {
        fun getDestinationByRoute(route: String?): MainActivityScreens {
            return when (route) {
                Library.route -> Library
                Authors.route -> Authors
                Settings.route -> Settings
                Book.route -> Book
                else -> Library
            }
        }
    }
}