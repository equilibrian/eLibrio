package su.elibrio.mobile.model

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import su.elibrio.mobile.model.fb.FictionBook
import timber.log.Timber
import java.io.File

/**
 * A manager class responsible for handling book-related operations.
 */
class BookManager {
    companion object {
        /**
         * Creates a `Book` instance from the provided file.
         *
         * @param file The file to create the book from.
         * @return A `Book` instance if the file format is supported, otherwise `null`.
         */
        private fun createBook(file: File): Book? {
            val format = SupportedFormat.from(file)

            return when (format) {
                SupportedFormat.FB2 -> createFictionBook(file)
                null -> null
            }
        }

        /**
         * Creates a `FictionBook` instance from the provided file.
         *
         * @param file The file to create the FictionBook from.
         * @return A `FictionBook` instance parsed from the file.
         */
        private fun createFictionBook(file: File): Book = FictionBook.from(file)

        /**
         * Scans the device for books and returns a list of found books.
         *
         * Performs a query on the device's external storage
         * to find files that match specific MIME types.
         *
         * @param ctx The context used to access the content resolver.
         * @return A list of books found on the device.
         * @throws IllegalArgumentException If a column does not exist in the cursor.
         */
        suspend fun scanDeviceForBooks(ctx: Context): MutableList<Book> {
            Timber.d("starting scanning device for books")
            return withContext(Dispatchers.IO) {
                val books = mutableListOf<Book>()
                val files = mutableListOf<File>()

                val projection = arrayOf(
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.DISPLAY_NAME,
                    MediaStore.Files.FileColumns.MIME_TYPE
                )
                val selection = MediaStore.Files.FileColumns.MIME_TYPE + " IN (?, ?)"
                val selectionArgs = arrayOf(
                    "application/x-fictionbook+xml",
                    "application/octet-stream"
                )
                val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
                val queryUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
                val cursor: Cursor? = ctx.contentResolver
                    .query(queryUri, projection, selection, selectionArgs, sortOrder)

                cursor?.use {
                    val dataIdx = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

                    while (it.moveToNext()) {
                        files.add(File(it.getString(dataIdx)))
                    }
                }

                files.forEach { file -> // TODO: временная мера, метод должен возвращать только список файлов
                    try {
                        createBook(file)?.let { books.add(it) }
                    } catch (ex: Exception) {
                        Timber.e(ex)
                    }
                }

                Timber.d("${files.count()} books found")

                books
            }
        }
    }
}