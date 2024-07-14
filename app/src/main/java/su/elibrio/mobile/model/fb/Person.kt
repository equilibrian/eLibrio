package su.elibrio.mobile.model.fb

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "person")
/**
 * Represents a person involved with the book.
 *
 * @property id The ID of the person.
 * @property firstName The first name of the person.
 * @property middleName The middle name of the person.
 * @property lastName The last name of the person.
 * @property nickname The nickname of the person.
 * @property homePages The home pages of the person.
 * @property emails The emails of the person.
 */
data class Person @JvmOverloads constructor(
    @param:Element(name = "id", required = false)
    @field:Element(name = "id", required = false)
    val id: String? = null,

    @param:Element(name = "first-name", required = false)
    @field:Element(name = "first-name", required = false)
    val firstName: String? = null,

    @param:Element(name = "middle-name", required = false)
    @field:Element(name = "middle-name", required = false)
    val middleName: String? = null,

    @param:Element(name = "last-name", required = false)
    @field:Element(name = "last-name", required = false)
    val lastName: String? = null,

    @param:Element(name = "nickname", required = false)
    @field:Element(name = "nickname", required = false)
    val nickname: String? = null,

    @param:ElementList(name = "home-page", inline = true, required = false, entry = "home-page")
    @field:ElementList(name = "home-page", inline = true, required = false, entry = "home-page")
    val homePages: List<String>? = null,

    @param:ElementList(name = "email", inline = true, required = false, entry = "email")
    @field:ElementList(name = "email", inline = true, required = false, entry = "email")
    val emails: List<String>? = null
) {
    fun getFullName(): String {
        return listOfNotNull(firstName, middleName, lastName).joinToString(" ")
    }
}