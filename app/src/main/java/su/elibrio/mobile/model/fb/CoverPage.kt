package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element

data class CoverPage(
    @field:Element(name = "image", required = false)
    @param:Element(name = "image", required = false)
    val image: Image?
)