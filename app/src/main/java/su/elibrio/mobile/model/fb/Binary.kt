package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Text

data class Binary(
    @field:Attribute(name = "id")
    @param:Attribute(name = "id")
    val id: String,

    @field:Attribute(name = "content-type")
    @param:Attribute(name = "content-type")
    val contentType: String,

    @field:Text
    @param:Text
    val value: String // Base64 content
)