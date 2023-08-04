package ru.equilibrian.elibrio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class LibraryScreenViewModel : ViewModel() {

    val books: LiveData<List<Pair<String, Painter>>>? = null

    var isFabVisible by mutableStateOf(false)
        private set

    fun hideFab() {
        isFabVisible = false
    }

    fun showFab() {
        isFabVisible = true
    }
}