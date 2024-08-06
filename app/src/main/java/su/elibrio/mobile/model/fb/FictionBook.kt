package su.elibrio.mobile.model.fb

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister
import su.elibrio.mobile.model.Book
import su.elibrio.mobile.utils.BitmapUtils
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
    override fun getBookTitle(): String = description.titleInfo.bookTitle

    override fun getAuthorFullName(): String? {
        val persons = description.titleInfo.authors
        val authors: StringBuilder = StringBuilder()
        persons.forEachIndexed { idx, person ->
            authors.append(if (idx == persons.lastIndex) person.getFullName() else ", ${person.getFullName()}")
        }

        return if (persons.isNotEmpty()) authors.toString() else null
    }

    override fun getBookSequence(): String? = description.titleInfo.sequence?.last()?.name

    override fun getBookPublisher(): String? = description.publishInfo?.publisher

    override fun getPublicationYear(): String? = description.publishInfo?.year

    override fun getBookLanguage(): String = description.titleInfo.lang

    override fun getTranslators(): List<String>? {
        val translators: List<Person> = description.titleInfo.translators ?: return null
        val result: MutableList<String> = mutableListOf()
        translators.forEach { translator ->
            result.add(translator.getFullName())
        }

        return result
    }

    override fun getIsbn(): String? = description.publishInfo?.isbn

    override fun getBookDescription(): String? {
        val paragraphs = description.titleInfo.annotation?.paragraph
        val description = paragraphs?.joinToString(separator = "\n") { it.value?.trim().orEmpty() }

        return description.takeIf { it.isNullOrBlank().not() }
    }

    override fun getCoverPage(ctx: Context): Bitmap? {
        val coverPageId: String? = description.titleInfo.coverPage?.image?.imageId

        val binary = binaries?.find { it.id == coverPageId }
        if (binary?.id == coverPageId) return binary?.value?.let { BitmapUtils.base64ToBitmap(it) }

        return null
    }

    companion object {
        private val serializer: Persister = Persister()

        /**
         * Constructs a [FictionBook] instance from XML content in a file.
         *
         * @param file The file containing XML data representing the fiction book.
         * @return A [FictionBook] instance parsed from the XML file.
         */
        suspend fun from(file: File): FictionBook {
            return withContext(Dispatchers.IO) {
                serializer.read(FictionBook::class.java, file)
            }
        }
    }
}