package guru.zoroark.pangoro

import kotlin.test.*

class TestReflectiveNodeDeclaration {
    data class SingleConstructor(val hello: String, val goodbye: Int) :
        PangoroNode {
        companion object :
            PangoroNodeDeclaration<SingleConstructor> by reflective()
    }

    @Test
    fun reflective_node_declaration_works_single_ctor() {
        val sc = SingleConstructor.make(
            PangoroTypeDescription(mapOf("hello" to "HI", "goodbye" to 42))
        )
        assertEquals("HI", sc.hello)
        assertEquals(42, sc.goodbye)
    }

    data class DoubleConstructor(val hello: String, val goodbye: Int) :
        PangoroNode {
        companion object :
            PangoroNodeDeclaration<DoubleConstructor> by reflective()

        @Suppress("unused")
        constructor(ciao: Int, buongiorno: String) : this(buongiorno, ciao)
    }

    @Test
    fun reflective_node_declaration_works_double_ctor() {
        val sc = DoubleConstructor.make(
            PangoroTypeDescription(
                mapOf(
                    "buongiorno" to "ARRIVEDERCI",
                    "ciao" to 765
                )
            )
        )
        assertEquals("ARRIVEDERCI", sc.hello)
        assertEquals(765, sc.goodbye)
    }

    @Test
    fun reflective_node_declaration_fails_on_no_match() {
        assertFailsWith<PangoroException> {
            DoubleConstructor.make(
                PangoroTypeDescription(
                    mapOf("buongiorno" to "ARRIVEDERCI", "goodbye" to 42)
                )
            )
        }.apply {
            val msg = message
            assertNotNull(msg)
            assertTrue(msg.contains("Could not find"))
        }
    }
}