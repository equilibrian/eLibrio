package ru.equilibrian.elibrio.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LibraryScreenViewModel : ViewModel() {
    var isFabVisible by mutableStateOf(false)
        private set

    fun hideFab() {
        isFabVisible = false
    }

    fun showFab() {
        isFabVisible = true
    }
}