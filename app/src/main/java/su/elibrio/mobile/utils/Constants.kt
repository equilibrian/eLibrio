package su.elibrio.mobile.utils

import su.elibrio.mobile.model.LibraryFilter
import su.elibrio.mobile.model.MainActivityScreens

val BOTTOM_NAVIGATION_DESTINATIONS = listOf(
    MainActivityScreens.Library,
    MainActivityScreens.Authors,
    MainActivityScreens.Settings,
)

val LIBRARY_FILTERS = listOf(
    LibraryFilter.All,
    LibraryFilter.Recent,
    LibraryFilter.Favourites
)