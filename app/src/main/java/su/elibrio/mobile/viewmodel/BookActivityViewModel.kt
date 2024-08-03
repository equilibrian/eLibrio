package su.elibrio.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.model.BookManager
import su.elibrio.mobile.model.database.repository.BookRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class BookActivityViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _isTitleVisible = MutableLiveData(false)
    val isTitleVisible: LiveData<Boolean> get() = _isTitleVisible

    private val _book = MutableLiveData<Book?>()
    val book: LiveData<Book?> get() = _book

    fun hideTitle() = _isTitleVisible.postValue(false)

    fun showTitle() = _isTitleVisible.postValue(true)

    fun findBook(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = BookManager.createBook(File(bookRepository.findById(id).src))
            _book.postValue(book)
        }
    }
}
