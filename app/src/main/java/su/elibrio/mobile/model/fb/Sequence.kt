package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Attribute

/**
 * Represents a sequence associated with the book.
 *
 * @property name The name of the sequence.
 * @property number The number in the sequence.
 */
data class Sequence(
    @field:Attribute(name = "name")
    @param:Attribute(name = "name")
    val name: String,

    @field:Attribute(name = "number", required = false)
    @param:Attribute(name = "number", required = false)
    val number: Int? = null
)