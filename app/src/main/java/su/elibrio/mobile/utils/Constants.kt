package su.elibrio.mobile.utils

import su.elibrio.mobile.model.LibraryFilter
import su.elibrio.mobile.model.Screen

val BOTTOM_NAVIGATION_DESTINATIONS = listOf(
    Screen.Library,
    Screen.Authors,
    Screen.Settings,
)

val LIBRARY_FILTERS = listOf(
    LibraryFilter.All,
    LibraryFilter.Recent,
    LibraryFilter.Favourites
)