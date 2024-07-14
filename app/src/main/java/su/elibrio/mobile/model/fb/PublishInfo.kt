package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

data class PublishInfo(
    @field:Element(name = "book-name", required = false)
    @param:Element(name = "book-name", required = false)
    val bookName: String? = null,

    @field:Element(name = "publisher", required = false)
    @param:Element(name = "publisher", required = false)
    val publisher: String? = null,

    @field:Element(name = "city", required = false)
    @param:Element(name = "city", required = false)
    val city: String? = null,

    @field:Element(name = "year", required = false)
    @param:Element(name = "year", required = false)
    val year: String? = null,

    @field:Element(name = "isbn", required = false)
    @param:Element(name = "isbn", required = false)
    val isbn: String? = null,

    @field:ElementList(name = "sequence", inline = true, required = false, entry = "sequence")
    @param:ElementList(name = "sequence", inline = true, required = false, entry = "sequence")
    val sequence: List<Sequence>? = null
)
