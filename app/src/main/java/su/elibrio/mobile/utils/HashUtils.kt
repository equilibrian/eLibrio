package su.elibrio.mobile.utils

import java.io.InputStream
import java.security.MessageDigest

object HashUtils {
    fun calculateSHA256Hash(inputStream: InputStream): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val buffer = ByteArray(8192)
        var read: Int

        while (inputStream.read(buffer).also { read = it } > 0) {
            digest.update(buffer, 0, read)
        }

        val sha256Hash = digest.digest()

        val hexString = StringBuilder()
        for (byte in sha256Hash) {
            hexString.append(String.format("%02x", byte))
        }

        return hexString.toString()
    }
}