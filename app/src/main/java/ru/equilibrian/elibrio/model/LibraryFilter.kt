package ru.equilibrian.elibrio.model

/**
 * Sealed class representing different filters for a library.
 *
 * This class provides a set of predefined filter instances that can be used to filter items in a library.
 *
 * Usage:
 * - `LibraryFilter.Recent` represents a filter for displaying recently added items in the library.
 * - `LibraryFilter.All` represents a filter for displaying all items in the library.
 * - `LibraryFilter.Newest` represents a filter for displaying the newest items in the library.
 * - `LibraryFilter.Favorites` represents a filter for displaying the user's favorite items in the library.
 * - `LibraryFilter.Unread` represents a filter for displaying unread items in the library.
 * - `LibraryFilter.ReadLater` represents a filter for displaying items that the user intends to read later.
 *
 * Note: It is recommended to use these predefined filter instances rather than creating new instances to ensure consistency
 * and proper comparison of filter objects.
 *
 * @param name The human-readable name of the filter.
 * @param filter The filter value used for identifying and applying the filter.
 */
sealed class LibraryFilter(val name: String, val filter: String) {
    /**
     * Represents a filter for displaying recently added items in the library.
     */
    object Recent : LibraryFilter("Recent", "recent")

    /**
     * Represents a filter for displaying all items in the library.
     */
    object All : LibraryFilter("All", "all")

    /**
     * Represents a filter for displaying the newest items in the library.
     */
    object Newest : LibraryFilter("Newest", "newest")

    /**
     * Represents a filter for displaying the user's favorite items in the library.
     */
    object Favorites : LibraryFilter("Favorites", "favorites")

    /**
     * Represents a filter for displaying unread items in the library.
     */
    object Unread : LibraryFilter("Unread", "unread")

    /**
     * Represents a filter for displaying items that the user intends to read later.
     */
    object ReadLater : LibraryFilter("Read later", "read_later")
}