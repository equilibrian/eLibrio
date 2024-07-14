package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

data class Description(
    @field:Element(name = "title-info")
    @param:Element(name = "title-info")
    val titleInfo: TitleInfo,

    @field:Element(name = "document-info", required = false) // TODO: required??
    @param:Element(name = "document-info", required = false)
    val documentInfo: DocumentInfo?,

    @field:Element(name = "publish-info", required = false)
    @param:Element(name = "publish-info", required = false)
    val publishInfo: PublishInfo?,

    @field:ElementList(name = "custom-info", inline = true, required = false, entry = "custom-info")
    @param:ElementList(name = "custom-info", inline = true, required = false, entry = "custom-info")
    val customInfo: List<CustomInfo>?
)