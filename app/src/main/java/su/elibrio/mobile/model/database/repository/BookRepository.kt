package su.elibrio.mobile.model.database.repository

import androidx.lifecycle.LiveData
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {

    fun getAll(): List<Book> = bookDao.getAll()

    fun getAllByIds(ids: IntArray): LiveData<List<Book>> = bookDao.loadAllByIds(ids)

    fun getAllHashes(): List<String> = bookDao.getAllHashes()

    fun findById(id: Int): Book = bookDao.findById(id)

    fun findBySequence(sequence: String): LiveData<List<Book>> = bookDao.findBySequence(sequence)

    fun findByHash(hash: String): LiveData<Book> = bookDao.findByHash(hash)

    suspend fun insert(book: Book) = bookDao.insert(book)

    suspend fun insertAll(books: List<Book>) = bookDao.insertAll(books)

    suspend fun delete(book: Book) = bookDao.delete(book)
}