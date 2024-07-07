package su.elibrio.mobile.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.model.BookManager

class LibraryScreenViewModel : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    fun scanForBooks(ctx: Context) {
        _inProgress.postValue(true)
        viewModelScope.launch {
            val foundBooks = BookManager.scanDeviceForBooks(ctx)
            _books.postValue(foundBooks)
            //_inProgress.postValue(false)
        }
    }
}