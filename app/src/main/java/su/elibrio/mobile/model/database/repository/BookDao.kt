package su.elibrio.mobile.model.database.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id IN (:bookIds)")
    fun loadAllByIds(bookIds: IntArray): List<Book>

    @Query("SELECT hash FROM book")
    fun getAllHashes(): List<String>

    @Query("SELECT * FROM book WHERE id = :id")
    fun findById(id: Int): Book

    @Query("SELECT * FROM book WHERE sequence LIKE :sequence")
    fun findAllBySequence(sequence: String): List<Book>

    @Query("SELECT * FROM book WHERE sequence LIKE :sequence AND id != :ignoreId")
    fun findAllBySequence(sequence: String, ignoreId: Int): List<Book>

    @Query("SELECT * FROM book WHERE hash LIKE :hash")
    fun findByHash(hash: String): Book

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg book: Book)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<Book>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBook(bookI: Book)

    @Delete
    suspend fun delete(book: Book)
}