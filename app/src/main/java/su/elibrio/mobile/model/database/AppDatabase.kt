package su.elibrio.mobile.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import su.elibrio.mobile.model.database.repository.Book
import su.elibrio.mobile.model.database.repository.BookDao

@Database(entities = [Book::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao() : BookDao
}