package su.elibrio.mobile.model

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A manager class responsible for handling book-related operations.
 */
class BookManager {
    companion object {
        private fun createBooks(filePaths: List<String>): MutableList<Book> {
            return mutableListOf()
        }

        /**
         * Scans the device for books and returns a list of found books.
         *
         * This function performs a query on the device's external storage
         * to find files that match specific MIME types. It runs the query
         * in a background thread using `Dispatchers.IO` to avoid blocking
         * the main thread.
         *
         * @param ctx The context used to access the content resolver.
         * @return A list of books found on the device.
         * @throws IllegalArgumentException If a column does not exist in the cursor.
         */
        suspend fun scanDeviceForBooks(ctx: Context): MutableList<Book> {
            return withContext(Dispatchers.IO) {
                val filePaths = mutableListOf<String>()
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
                        filePaths.add(it.getString(dataIdx))
                    }
                }

                createBooks(filePaths)
            }
        }
    }
}