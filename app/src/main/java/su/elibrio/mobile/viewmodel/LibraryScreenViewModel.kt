package su.elibrio.mobile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import su.elibrio.mobile.model.Book

class LibraryScreenViewModel : ViewModel() {
    val books: LiveData<List<Book>>? = null

    var inProgress by mutableStateOf(false)
        private set
}