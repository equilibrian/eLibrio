package su.elibrio.mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.model.database.repository.BookRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookScreenViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _isTitleVisible = MutableLiveData(false)
    val isTitleVisible: LiveData<Boolean> get() = _isTitleVisible

    private val _book = MutableLiveData<Book?>()
    val book: LiveData<Book?> get() = _book

    private val _otherBooks = MutableLiveData<List<Book>>()
    val otherBooks: LiveData<List<Book>> get() = _otherBooks

    fun hideTitle() = _isTitleVisible.postValue(false)

    fun showTitle() = _isTitleVisible.postValue(true)

    fun findBook(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = bookRepository.findById(id)
            _book.postValue(book)
            if (book.sequence != null) {
                Timber.d("Sequence: ${book.sequence}")
                val otherBooks = bookRepository.findAllBySequence(book.sequence, id)
                otherBooks.forEach { b ->
                    Timber.d("Found in series: ${b.title}")
                }
                _otherBooks.postValue(otherBooks)
            }
        }
    }

    fun updateFavourStatus(isFavourite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentBook: Book? = _book.value
            if (currentBook != null) {
                val updatedBook = currentBook.copy(isFavourite = isFavourite)
                bookRepository.updateBook(updatedBook)
                _book.postValue(updatedBook)
            }
        }
    }
}
