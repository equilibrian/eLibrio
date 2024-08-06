package su.elibrio.mobile.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.model.database.repository.BookDao

/**
 * The Room database that holds the application data.
 */
@Database(entities = [Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the data access object (DAO) for books.
     *
     * @return The [BookDao] instance.
     */
    abstract fun bookDao() : BookDao
}