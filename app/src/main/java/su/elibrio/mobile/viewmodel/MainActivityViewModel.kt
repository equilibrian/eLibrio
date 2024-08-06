package su.elibrio.mobile.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import su.elibrio.mobile.exceptions.UnsupportedBookException
import su.elibrio.mobile.model.BookManager
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.model.database.repository.BookRepository
import su.elibrio.mobile.utils.BitmapUtils
import su.elibrio.mobile.utils.HashUtils
import su.elibrio.mobile.utils.Utils.sizeInMb
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _inProgress = MutableLiveData(true)
    val inProgress: LiveData<Boolean> get() = _inProgress

    private val _isFiltersVisible = MutableLiveData(true)
    val isFiltersVisible: LiveData<Boolean> get() = _isFiltersVisible

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    suspend fun scanForBooks(ctx: Context) {
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
                    val coverPage: Bitmap? = book.getCoverPage(ctx)
                    val newBook = Book(
                        coverPageSrc = BitmapUtils.saveBitmap(ctx, coverPage, hash),
                        title = book.getBookTitle(),
                        author = book.getAuthorFullName(),
                        annotation = book.getBookDescription(),
                        sequence = book.getBookSequence(),
                        publisher = book.getBookPublisher(),
                        year = book.getPublicationYear(),
                        lang = book.getBookLanguage(),
                        translator = book.getTranslators()?.joinToString(", "),
                        isbn = book.getIsbn(),
                        format = file.extension,
                        size = "${file.sizeInMb} Mb",
                        src = src,
                        hash = hash
                    )
                    books.add(newBook)
                }
            } catch (ex: UnsupportedBookException) {
                Timber.i(ex, "File: $src")
            } catch (ex: Exception) {
                Timber.e(ex, "File: $src")
            }
        }

        if (books.isNotEmpty()) bookRepository.insertAll(books)

        _books.postValue(bookRepository.getAll())
        _inProgress.postValue(false)
    }

    fun hideFilters() = _isFiltersVisible.postValue(false)

    fun showFilters() = _isFiltersVisible.postValue(true)
}
