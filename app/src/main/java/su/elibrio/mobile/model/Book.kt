package su.elibrio.mobile.model

import android.content.Context
import android.graphics.Bitmap

interface Book {
    fun getBookTitle(): String
    fun getCoverPage(ctx: Context): Bitmap
}
