package su.elibrio.mobile.model

sealed class Book(
    open val id: Long,
    open val title: String,
    open val annotation: String,
    open val coverPage: String,
    open val filePath: String,
    open val mimeType: String,
    open val fileSize: Long
) {

    data class FictionBook(
        override val id: Long,
        override val title: String,
        override val annotation: String,
        override val coverPage: String,
        override val filePath: String,
        override val mimeType: String,
        override val fileSize: Long
    ) : Book(id, title, annotation, coverPage, filePath, mimeType, fileSize) {

    }
}
