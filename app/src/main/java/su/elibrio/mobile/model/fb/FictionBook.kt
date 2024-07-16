package su.elibrio.mobile.model.fb

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister
import su.elibrio.mobile.R
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.utils.Utils
import java.io.File

/**
 * Represents a FictionBook.
 *
 * @property description The description of the FictionBook.
 * @property binaries The list of binaries associated with the FictionBook.
 */
@Root(name = "FictionBook", strict = false)
data class FictionBook(
    @field:Element(name = "description")
    @param:Element(name = "description")
    val description: Description,

    /**@field:ElementList(name = "body")
    val body: List<Body>,*/

    @field:ElementList(name = "binary", inline = true, required = false, entry = "binary")
    @param:ElementList(name = "binary", inline = true, required = false, entry = "binary")
    val binaries: List<Binary>?
) : Book {
    companion object {
        private val serializer: Persister = Persister()

        /**
         * Constructs a [FictionBook] instance from XML content in a file.
         *
         * @param file The file containing XML data representing the fiction book.
         * @return A [FictionBook] instance parsed from the XML file.
         */
        fun from(file: File): FictionBook = serializer.read(FictionBook::class.java, file)
    }

    override fun getBookTitle(): String = description.titleInfo.bookTitle

    override fun getCoverPage(ctx: Context): Bitmap {
        val noCover: Bitmap = ctx.getDrawable(R.drawable.no_cover)?.toBitmap()!!
        val coverPageId: String = description.titleInfo.coverPage?.image?.imageId ?: return noCover

        val binary = binaries?.find { it.id == coverPageId }
        if (binary?.id == coverPageId) return Utils.base64ToBitmap(binary.value)

        return noCover
    }
}