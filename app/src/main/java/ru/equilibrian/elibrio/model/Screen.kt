package ru.equilibrian.elibrio.model

import ru.equilibrian.elibrio.R

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Library : Screen("library", "Library", R.drawable.book_fill)
    object Hub : Screen("hub", "Hub", R.drawable.darhboard_fill)
    object Authors : Screen("authors", "Authors", R.drawable.authors_fill)
}