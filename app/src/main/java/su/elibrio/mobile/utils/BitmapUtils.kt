package su.elibrio.mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

/**
 * A class containing bitmap utility methods.
 */
object BitmapUtils {
    /**
     * Converts a Base64 encoded string to a `Bitmap`
     *
     * @param base64String The Base64 encoded string
     * @return `Bitmap`
     */
    fun base64ToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    /**
     * Saves a bitmap image to the device's internal storage.
     *
     * @param ctx The context used to access the file system.
     * @param bitmap The bitmap image to save.
     * @param fileName The name of the file to save the bitmap as.
     * @return The absolute path of the saved image, or null if an error occurred.
     */
    fun saveBitmap(ctx: Context, bitmap: Bitmap?, fileName: String): String? {
        return try {
            val dir = File(ctx.filesDir, "cover_pages")
            if (!dir.exists()) dir.mkdirs()

            val file = File(dir, "$fileName.png")
            FileOutputStream(file).use { fos ->
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
            }

            file.absolutePath
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }
    }
}