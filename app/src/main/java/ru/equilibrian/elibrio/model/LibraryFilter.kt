package ru.equilibrian.elibrio.model

sealed class LibraryFilter(val name: String, val filter: String) {
    object Recent : LibraryFilter("Recent", "recent")
    object All : LibraryFilter("All", "all")
    object Newest : LibraryFilter("Newest", "newest")
    object Favorites : LibraryFilter("Favorites", "favorites")
    object Unread : LibraryFilter("Unread", "unread")
    object ReadLater : LibraryFilter("Read later", "read_later")
}