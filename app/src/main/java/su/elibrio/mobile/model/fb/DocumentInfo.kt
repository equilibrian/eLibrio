package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "document-info", strict = false)
data class DocumentInfo(
    @field:ElementList(name = "author", inline = true, required = false, entry = "author")
    @param:ElementList(name = "author", inline = true, required = false, entry = "author")
    val authors: List<Person>? = null,

    @field:Element(name = "program-used", required = false)
    @param:Element(name = "program-used", required = false)
    val programUsed: String? = null,

    @field:Element(name = "date", required = false)
    @param:Element(name = "date", required = false)
    val date: String? = null,

    @field:ElementList(name = "src-url", required = false, entry = "src-url")
    @param:ElementList(name = "src-url", required = false, entry = "src-url")
    val srcUls: List<String>? = null,

    @field:Element(name = "src-ocr", required = false)
    @param:Element(name = "src-ocr", required = false)
    val srcOcr: String? = null,

    @field:Element(name = "id", required = false)
    @param:Element(name = "id", required = false)
    val id: String? = null,

    @field:Element(name = "version", required = false)
    @param:Element(name = "version", required = false)
    val version: String? = null,

    @field:Element(name = "history", required = false)
    @param:Element(name = "history", required = false)
    val history: Annotation? = null, // TODO: http://www.fictionbook.org/index.php/Элемент_history

    @field:Element(name = "publisher", required = false)
    @param:Element(name = "publisher", required = false)
    val publisher: Person? = null
)
