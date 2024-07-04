package su.elibrio.mobile.model

import su.elibrio.mobile.R

sealed class LibraryFilter(val title: Int, val filter: String) {
    data object All : LibraryFilter(R.string.st_filter_all, "all")
    data object Recent : LibraryFilter(R.string.st_filter_recent, "recent")
    data object Favourites: LibraryFilter(R.string.st_filter_favourites, "favourites")
}