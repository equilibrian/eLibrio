package su.elibrio.mobile.model.database.repository

import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {

    fun getAll(): List<Book> = bookDao.getAll()

    fun getAllByIds(ids: IntArray): List<Book> = bookDao.loadAllByIds(ids)

    fun getAllHashes(): List<String> = bookDao.getAllHashes()

    fun findById(id: Int): Book = bookDao.findById(id)

    fun findAllBySequence(sequence: String): List<Book> = bookDao.findAllBySequence(sequence)

    fun findAllBySequence(sequence: String, ignoreId: Int): List<Book> =
        bookDao.findAllBySequence(sequence, ignoreId)

    fun findByHash(hash: String): Book = bookDao.findByHash(hash)

    suspend fun insert(book: Book) = bookDao.insert(book)

    suspend fun insertAll(books: List<Book>) = bookDao.insertAll(books)

    suspend fun updateBook(book: Book) = bookDao.updateBook(book)

    suspend fun delete(book: Book) = bookDao.delete(book)
}