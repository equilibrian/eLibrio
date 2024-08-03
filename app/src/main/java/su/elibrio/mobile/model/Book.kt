package su.elibrio.mobile.model

import android.content.Context
import android.graphics.Bitmap

interface Book {
    fun getCoverPage(ctx: Context): Bitmap

    fun getBookTitle(): String

    fun getAuthorFullName(): String?

    fun getBookSequence(): String?

    fun getBookDescription(): String?
}
