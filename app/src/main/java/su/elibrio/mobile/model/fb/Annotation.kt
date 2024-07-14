package su.elibrio.mobile.model.fb

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import su.elibrio.mobile.model.fb.font.Paragraph

@Root(name = "annotation", strict = false)
/**
 * Represents annotation or descriptive information associated with a book in FictionBook format.
 *
 * @property paragraph List of paragraphs containing the annotated text.
 */
data class Annotation(
    // TODO: http://www.fictionbook.org/index.php/Элемент_annotation
    @field:ElementList(name = "p", inline = true, entry = "p")
    @param:ElementList(name = "p", inline = true, entry = "p")
    val paragraph: List<Paragraph>
)