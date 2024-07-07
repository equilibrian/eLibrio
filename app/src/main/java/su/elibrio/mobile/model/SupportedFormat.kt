package su.elibrio.mobile.model

import timber.log.Timber
import java.io.File

enum class SupportedFormat(val extension: String, val mimeTypes: List<String>) {
    FB2("fb2", listOf("application/x-fictionbook+xml", "application/octet-stream"));

    companion object {
        fun fromFile(file: File): SupportedFormat? {
            val ext = file.extension.lowercase()
            val mimeType = getMimeType(ext)
            Timber.tag("SupportedFormat").d("Extension: %s\nmime-type: %s", ext, mimeType)

            return entries.find { format ->
                format.extension == ext && format.mimeTypes.contains(mimeType)
            }
        }

        private fun getMimeType(extension: String): String? {
            //return MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)

            // Получение MIME-типа файла, может использовать библиотеку или ContentResolver.
            return when (extension) {
                "fb2" -> "application/x-fictionbook+xml"
                "epub" -> "application/epub+zip"
                "mobi" -> "application/x-mobipocket-ebook"
                else -> null
            }
        }
    }
}