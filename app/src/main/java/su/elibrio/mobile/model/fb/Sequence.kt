package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Attribute

data class Sequence(
    @field:Attribute(name = "name")
    @param:Attribute(name = "name")
    val name: String,

    @field:Attribute(name = "number", required = false)
    @param:Attribute(name = "number", required = false)
    val number: Int? = null // or String?
)