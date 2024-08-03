package su.elibrio.mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.drawable.toBitmap
import su.elibrio.mobile.R
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

/**
 * A class containing utility methods.
 */
class Utils {
    companion object {
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

        fun saveBitmap(ctx: Context, bitmap: Bitmap, fileName: String): String? {
            return try {
                val dir = File(ctx.filesDir, "cover_pages")
                if (!dir.exists()) dir.mkdirs()

                val file = File(dir, "$fileName.png")
                FileOutputStream(file).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                }

                file.absolutePath
            } catch (ex: Exception) {
                Timber.e(ex)
                null
            }
        }


        fun loadBitmap(ctx: Context, src: String?): Bitmap {
            val noCover: Bitmap = ctx.getDrawable(R.drawable.no_cover)?.toBitmap()!!
            return try {
                val file: File? = src?.let { File(it) }
                if (file?.exists() == true) BitmapFactory.decodeFile(file.absolutePath) else noCover
            } catch (ex: Exception) {
                Timber.e(ex)
                noCover
            }
        }
    }
}