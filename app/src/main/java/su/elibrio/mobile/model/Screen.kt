package su.elibrio.mobile.model

import su.elibrio.mobile.R

sealed class Screen(val route: String, val titleStrId: Int, val icon: Int) {
    data object Collection : Screen("collection", R.string.collection_screen_title, R.drawable.collection_icon)
    data object Authors : Screen("authors", R.string.authors_screen_title, R.drawable.authors_icon)
    data object Settings : Screen("settings", R.string.settings_screen_title, R.drawable.setting_fill)
}