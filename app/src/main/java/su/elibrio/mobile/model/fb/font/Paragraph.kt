package su.elibrio.mobile.model.fb.font

import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = "p", strict = false)
data class Paragraph(
    @field:Text(required = false)
    @param:Text(required = false)
    val value: String? = null
)