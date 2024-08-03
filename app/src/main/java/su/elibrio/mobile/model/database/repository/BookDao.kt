package su.elibrio.mobile.model.database.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAll(): List<Book>

    @Query("SELECT * FROM book WHERE id IN (:bookIds)")
    fun loadAllByIds(bookIds: IntArray): LiveData<List<Book>>

    @Query("SELECT hash FROM book")
    fun getAllHashes(): List<String>

    @Query("SELECT * FROM book WHERE id = :id")
    fun findById(id: Int): Book

    @Query("SELECT * FROM book WHERE sequence LIKE :sequence")
    fun findBySequence(sequence: String): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE hash LIKE :hash")
    fun findByHash(hash: String): LiveData<Book>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg book: Book)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(books: List<Book>)

    @Delete
    fun delete(book: Book)
}