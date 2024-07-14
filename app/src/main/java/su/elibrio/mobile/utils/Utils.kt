package su.elibrio.mobile.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import timber.log.Timber

/**
 * A class containing utility methods.
 */
class Utils {
    companion object {
        private val TAG: String = this::class.java.name

        /**
         * Converts a Base64 encoded string to a `Bitmap`.
         *
         * @param base64String The Base64 encoded string.
         * @return A `Bitmap` if the conversion is successful, otherwise `null`.
         */
        fun base64ToBitmap(base64String: String): Bitmap? {
            return try {
                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            } catch (ex: IllegalArgumentException) {
                Timber.tag(TAG).e(ex)
                null
            }
        }
    }
}