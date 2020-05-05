package guru.zoroark.pangoro

import guru.zoroark.pangoro.expectations.PangoroExpectedNode
import kotlin.test.*

class ExpectedNodeTest {
    class One : PangoroNode {
        companion object : PangoroNodeDeclaration<One> {
            override fun make(args: PangoroTypeDescription): One = One()
        }
    }

    class Two : PangoroNode {
        companion object : PangoroNodeDeclaration<Two> {
            override fun make(args: PangoroTypeDescription): Two = Two()
        }
    }

    @Test
    fun test_undefined_expected_node_fails() {
        val exp = PangoroExpectedNode(Two, null)
        assertFailsWith<PangoroException> {
            exp.matches(
                PangoroParsingContext(
                    listOf(),
                    mapOf(One to PangoroDescribedType(One, listOf()))
                ),
                0
            )
        }.apply {
            val msg = message
            assertNotNull(msg)
            assertTrue(
                msg.containsJvm("Two") && msg.contains("declared"),
                "Expected 'Two' and 'declared' in exception message $msg"
            )
        }
    }

    @Test
    fun test_successful() {
        val exp = PangoroExpectedNode(Two, "yeet")
        val res = exp.matches(
            PangoroParsingContext(
                listOf(),
                mapOf(Two to PangoroDescribedType(Two, listOf()))
            ), 0
        )
        assertTrue(res is ExpectationResult.Success)
        assertTrue(res.stored["yeet"] is Two)
    }
}