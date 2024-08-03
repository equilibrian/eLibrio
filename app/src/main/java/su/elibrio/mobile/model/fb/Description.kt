package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

/**
 * Represents the description of a book.
 *
 * @property titleInfo The title information of the book.
 * @property documentInfo The document information of the book.
 * @property publishInfo The publishing information of the book.
 * @property customInfo The custom information of the book.
 */
data class Description(
    @field:Element(name = "title-info")
    @param:Element(name = "title-info")
    val titleInfo: TitleInfo,

    @field:Element(name = "document-info", required = false)
    @param:Element(name = "document-info", required = false)
    val documentInfo: DocumentInfo?,

    @field:Element(name = "publish-info", required = false)
    @param:Element(name = "publish-info", required = false)
    val publishInfo: PublishInfo?,

    @field:ElementList(name = "custom-info", inline = true, required = false, entry = "custom-info")
    @param:ElementList(name = "custom-info", inline = true, required = false, entry = "custom-info")
    val customInfo: List<CustomInfo>?
)