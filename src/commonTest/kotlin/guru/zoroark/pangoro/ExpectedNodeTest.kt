package guru.zoroark.pangoro

import kotlin.test.*

class ExpectedNodeTest {
    class One() : PangoroNode {
        companion object : PangoroNodeDeclaration<One> {
            override fun make(args: PangoroTypeDescription): One = One()
        }
    }

    class Two() : PangoroNode {
        companion object : PangoroNodeDeclaration<Two> {
            override fun make(args: PangoroTypeDescription): Two = Two()
        }
    }

    @Test
    fun `Test undefined expected node fails`() {
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
                msg.contains("Two") && msg.contains("declared"),
                "Expected 'Two' and 'declared' in exception message $msg"
            )
        }
    }

    @Test
    fun `Test successful`() {
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