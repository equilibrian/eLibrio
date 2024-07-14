package su.elibrio.mobile.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister
import su.elibrio.mobile.model.fb.Binary
import su.elibrio.mobile.model.fb.Description
import java.io.File
import java.io.InputStream

sealed class Book{
    @Root(name = "FictionBook", strict = false)
    data class FictionBook(
        @field:Element(name = "description")
        @param:Element(name = "description")
        val description: Description,

        /**@field:ElementList(name = "body")
        val body: List<Body>,*/

        @field:ElementList(name = "binary", inline = true, required = false, entry = "binary")
        @param:ElementList(name = "binary", inline = true, required = false, entry = "binary")
        val binaries: List<Binary>?
    ) : Book() {
        companion object {
            private val serializer: Persister = Persister()

            /**
             * Constructs a [FictionBook] instance from XML content in a file.
             *
             * @param file The file containing XML data representing the fiction book.
             * @return A [FictionBook] instance parsed from the XML file.
             */
            fun from(file: File): FictionBook = serializer.read(FictionBook::class.java, file)

            /**
             * Constructs a [FictionBook] instance from XML content in a string.
             *
             * @param xml The XML string representing the fiction book.
             * @return A [FictionBook] instance parsed from the XML string.
             */
            fun from(xml: String): FictionBook = serializer.read(FictionBook::class.java, xml)

            /**
             * Constructs a [FictionBook] instance from XML content in an input stream.
             *
             * @param stream The InputStream containing XML data representing the fiction book.
             * @return A [FictionBook] instance parsed from the XML input stream.
             */
            fun from(stream: InputStream): FictionBook = serializer.read(FictionBook::class.java, stream)
        }
    }
}
