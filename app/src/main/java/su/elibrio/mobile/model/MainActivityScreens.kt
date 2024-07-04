package su.elibrio.mobile.model

import su.elibrio.mobile.R

sealed class MainActivityScreens(val route: String, val titleStrId: Int, val icon: Int) {
    data object Library : MainActivityScreens("library", R.string.library_screen_title, R.drawable.collection_icon)
    data object Authors : MainActivityScreens("authors", R.string.authors_screen_title, R.drawable.authors_icon)
    data object Settings : MainActivityScreens("settings", R.string.settings_screen_title, R.drawable.setting_fill)
}