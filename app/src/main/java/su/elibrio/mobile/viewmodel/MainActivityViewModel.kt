package su.elibrio.mobile.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.model.BookManager

class MainActivityViewModel : ViewModel() {
    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _isFiltersVisible = MutableLiveData(true)
    val isFiltersVisible: LiveData<Boolean> get() = _isFiltersVisible

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    fun scanForBooks(ctx: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _inProgress.postValue(true)
            val foundBooks = BookManager.scanDeviceForBooks(ctx).toList()
            _books.postValue(foundBooks)
            _inProgress.postValue(false)
        }
    }

    fun hideFilters() = _isFiltersVisible.postValue(false)

    fun showFilters() = _isFiltersVisible.postValue(true)
}
