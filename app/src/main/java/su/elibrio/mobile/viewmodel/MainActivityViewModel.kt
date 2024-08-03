package su.elibrio.mobile.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import su.elibrio.mobile.exceptions.UnsupportedBookException
import su.elibrio.mobile.model.BookManager
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.model.database.repository.BookRepository
import su.elibrio.mobile.utils.HashUtils
import su.elibrio.mobile.utils.Utils
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _inProgress = MutableLiveData(false)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _isFiltersVisible = MutableLiveData(true)
    val isFiltersVisible: LiveData<Boolean> get() = _isFiltersVisible

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    fun scanForBooks(ctx: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _inProgress.postValue(true)
            val foundSrcs = BookManager.scanDeviceForBooks(ctx)
            val books = mutableListOf<Book>()
            val hashes: List<String> = bookRepository.getAllHashes()

            foundSrcs.forEach { src ->
                try {
                    val file = File(src)
                    val hash = file.inputStream().use { stream -> HashUtils.calculateSHA256Hash(stream) }

                    if (hash !in hashes) {
                        val book = BookManager.createBook(file)
                        val coverPage: Bitmap = book.getCoverPage(ctx)
                        val newBook = Book(
                            coverPageSrc = Utils.saveBitmap(ctx, coverPage, hash),
                            title = book.getBookTitle(),
                            sequence = book.getBookSequence(),
                            src = src,
                            hash = hash
                        )
                        books.add(newBook)
                    }
                } catch (ex: UnsupportedBookException) {
                    Timber.e(ex, "File: %s", src)
                }
            }

            if (books.isNotEmpty()) bookRepository.insertAll(books)

            _books.postValue(bookRepository.getAll())
            _inProgress.postValue(false)
        }
    }

    fun hideFilters() = _isFiltersVisible.postValue(false)

    fun showFilters() = _isFiltersVisible.postValue(true)
}
