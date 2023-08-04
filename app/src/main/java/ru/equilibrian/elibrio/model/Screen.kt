package ru.equilibrian.elibrio.model

import ru.equilibrian.elibrio.R

/**
 * Sealed class representing different screens in the application.
 *
 * This class provides a set of predefined screen instances that represent different screens or destinations in the app.
 * Each screen has a unique route identifier, a human-readable title, and an associated icon resource ID.
 *
 * Usage:
 * - `Screen.Library` represents the library screen.
 * - `Screen.Hub` represents the hub screen.
 * - `Screen.Authors` represents the authors screen.
 *
 * Note: It is recommended to use these predefined screen instances rather than creating new instances to ensure consistency
 * and proper comparison of screen objects.
 *
 * @param route The unique route identifier for the screen.
 * @param title The human-readable title of the screen.
 * @param icon The icon resource ID associated with the screen.
 */
sealed class Screen(val route: String, val title: String, val icon: Int) {
    /**
     * Represents the library screen.
     */
    object Library : Screen("library", "Library", R.drawable.book_fill)

    /**
     * Represents the hub screen.
     */
    object Hub : Screen("hub", "Hub", R.drawable.dashboard_fill)

    /**
     * Represents the authors screen.
     */
    object Authors : Screen("authors", "Authors", R.drawable.authors_fill)
}