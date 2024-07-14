package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "title-info")
/**
 * Represents the title information of a book.
 *
 * @property genre The genres of the book.
 * @property authors The authors of the book.
 * @property bookTitle The title of the book.
 * @property annotation The annotation of the book.
 * @property keywords The keywords associated with the book.
 * @property date The date of the book.
 * @property lang The language of the book.
 * @property srcLang The source language of the book.
 * @property coverPage The cover page of the book.
 * @property translators The translators of the book.
 * @property sequence The sequence information of the book.
 */
data class TitleInfo(
    @field:ElementList(name = "genre", inline = true, required = false, entry = "genre")
    @param:ElementList(name = "genre", inline = true, required = false, entry = "genre")
    var genre: MutableList<String>? = mutableListOf(),

    @field:ElementList(name = "author", inline = true, entry = "author")
    @param:ElementList(name = "author", inline = true, entry = "author")
    val authors: List<Person>,

    @field:Element(name = "book-title")
    @param:Element(name = "book-title")
    val bookTitle: String,

    @field:Element(name = "annotation", required = false)
    @param:Element(name = "annotation", required = false)
    val annotation: Annotation?,

    @field:Element(name = "keywords", required = false)
    @param:Element(name = "keywords", required = false)
    val keywords: String?,

    @field:Element(name = "date", required = false)
    @param:Element(name = "date", required = false)
    val date: String?,

    @field:Element(name = "lang")
    @param:Element(name = "lang")
    val lang: String,

    @field:Element(name = "src-lang", required = false)
    @param:Element(name = "src-lang", required = false)
    val srcLang: String?,

    @field:Element(name = "coverpage", required = false)
    @param:Element(name = "coverpage", required = false)
    val coverPage: CoverPage?,

    @field:ElementList(name = "translator", inline = true, required = false, entry = "translator")
    @param:ElementList(name = "translator", inline = true, required = false, entry = "translator")
    val translators: List<Person>?,

    @field:ElementList(name = "sequence", inline = true, required = false, entry = "sequence")
    @param:ElementList(name = "sequence", inline = true, required = false, entry = "sequence")
    val sequence: List<Sequence>?,
) {

    init {
        clearGenres()
    }

    /**
     * Removes empty strings from the [genre] list.
     */
    private fun clearGenres() {
        if (genre.isNullOrEmpty()) return

        genre!!.forEachIndexed { idx, item ->
            if (item.isEmpty()) genre!!.removeAt(idx)

            genre!![idx].replace("-", "_")
        }
    }
}