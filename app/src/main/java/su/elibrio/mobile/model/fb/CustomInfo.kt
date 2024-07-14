package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Text

data class CustomInfo(
    @field:Attribute(name = "info-type")
    @param:Attribute(name = "info-type")
    val infoType: String,

    @field:Text(required = false)
    @param:Text(required = false)
    val value: String?
)