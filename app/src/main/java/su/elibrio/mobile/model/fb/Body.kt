package su.elibrio.mobile.model.fb

import org.simpleframework.xml.ElementList
import su.elibrio.mobile.model.fb.font.Paragraph

data class Body(
    @field:ElementList(entry = "p", inline = true, required = false)
    @param:ElementList(entry = "p", inline = true, required = false)
    val paragraphs: List<Paragraph>? = null,
)
