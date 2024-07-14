package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Namespace

data class Image(
    @field:Attribute(name = "type", required = false)
    @field:Namespace(reference = "http://www.w3.org/1999/xlink")
    @param:Attribute(name = "type", required = false)
    @param:Namespace(reference = "http://www.w3.org/1999/xlink")
    var type: String?,

    @field:Attribute(name = "href")
    @field:Namespace(reference = "http://www.w3.org/1999/xlink")
    @param:Attribute(name = "href")
    @param:Namespace(reference = "http://www.w3.org/1999/xlink")
    var imageId: String,

    @field:Attribute(name = "alt", required = false)
    @param:Attribute(name = "alt", required = false)
    var alt: String?,

    @field:Attribute(name = "title", required = false)
    @param:Attribute(name = "title", required = false)
    var title: String?,

    @field:Attribute(name = "id", required = false)
    @param:Attribute(name = "id", required = false)
    var id: String?
) {
    init {
        imageId = imageId.removePrefix("#")
    }
}