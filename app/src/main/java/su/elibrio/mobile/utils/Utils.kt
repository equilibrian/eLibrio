package su.elibrio.mobile.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

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
    }
}