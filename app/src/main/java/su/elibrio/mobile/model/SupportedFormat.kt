package su.elibrio.mobile.model

import java.io.File

/**
 * Enum class representing supported e-book formats.
 *
 * @property extension The file extension associated with the format.
 * @property mimeTypes The list of MIME types associated with the format.
 */
enum class SupportedFormat(val extension: String, val mimeTypes: List<String>) {
    FB2("fb2", listOf("application/x-fictionbook+xml", "application/octet-stream"));

    companion object {
        /**
         * Determines the supported format of a given file.
         *
         * @param file The file to check.
         * @return The [SupportedFormat] if the file matches a supported format, or null otherwise.
         */
        fun from(file: File): SupportedFormat? {
            val ext = file.extension.lowercase()
            val mimeType = getMimeType(ext)

            return entries.find { format ->
                format.extension == ext && format.mimeTypes.contains(mimeType)
            }
        }

        /**
         * Retrieves the MIME type associated with a given file extension.
         *
         * @param extension The file extension.
         * @return The MIME type as a [String], or null if the extension is not recognized.
         */
        private fun getMimeType(extension: String): String? {
            return when (extension) {
                "fb2" -> "application/x-fictionbook+xml"
                "epub" -> "application/epub+zip"
                "mobi" -> "application/x-mobipocket-ebook"
                else -> null
            }
        }
    }
}