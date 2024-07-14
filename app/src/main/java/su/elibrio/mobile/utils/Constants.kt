package su.elibrio.mobile.utils

import su.elibrio.mobile.model.LibraryFilter
import su.elibrio.mobile.model.MainActivityScreens

val MAIN_ACTIVITY_SCREENS = listOf(
    MainActivityScreens.Library,
    MainActivityScreens.Authors,
    MainActivityScreens.Settings
)

val LIBRARY_FILTERS = listOf(
    LibraryFilter.All,
    LibraryFilter.Recent,
    LibraryFilter.Favourites
)