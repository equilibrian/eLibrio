package su.elibrio.mobile.model

import android.content.Context
import android.graphics.Bitmap

interface Book {
    fun getCoverPage(ctx: Context): Bitmap?

    fun getBookTitle(): String

    fun getAuthorFullName(): String?

    fun getBookSequence(): String?

    fun getBookPublisher(): String?

    fun getPublicationYear(): String?

    fun getBookLanguage(): String?

    fun getTranslators(): List<String>?

    fun getIsbn(): String?

    fun getBookDescription(): String?
}
