package guru.zoroark.pangoro

import kotlin.test.*

class TypeDescriptionTest {
    @Test
    fun argument_retrieval_works() {
        val ptd = PangoroTypeDescription(mapOf("One" to "Two"))
        val str: String = ptd["One"]
        assertEquals("Two", str)
    }

    @Test
    fun argument_retrieval_wrong_type_fails() {
        val ptd = PangoroTypeDescription(mapOf("One" to "Two"))
        assertFailsWith<PangoroException> {
            val str: Int = ptd["One"]
            println("But $str is not an integer!") // Should not happen
        }.apply {
            val msg = message
            assertNotNull(msg)
            assertTrue(msg.contains("type"))
        }
    }

    @Test
    fun argument_retrieval_key_absent_fails() {
        val ptd = PangoroTypeDescription(mapOf("One" to "Two"))
        assertFailsWith<PangoroException> {
            val str: String = ptd["Three"]
            println("But $str does not exist!") // Should not happen
        }.apply {
            val msg = message
            assertNotNull(msg)
            assertTrue(msg.contains("does not exist") && msg.contains("Three"))
        }
    }

}