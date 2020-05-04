package guru.zoroark.pangoro

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches
import guru.zoroark.lixy.tokenType
import kotlin.test.*

class ParserTest {

    class SimpleType : PangoroNode {
        companion object : PangoroNodeDeclaration<SimpleType> {
            override fun make(args: PangoroTypeDescription) = SimpleType()
        }
    }

    class StoringType(val child: SimpleType) : PangoroNode {
        companion object : PangoroNodeDeclaration<StoringType> {
            override fun make(args: PangoroTypeDescription) = StoringType(
                args["child"]
            )
        }
    }

    @Test
    fun `Storing parser test`() {
        val tokenOne = tokenType()
        val parser = PangoroParser(
            types = listOf(
                PangoroDescribedType(
                    type = SimpleType,
                    expectations = listOf(PangoroExpectedLixyToken(tokenOne))
                ),
                PangoroDescribedType(
                    type = StoringType,
                    expectations = listOf(
                        PangoroExpectedNode(
                            SimpleType,
                            "child"
                        )
                    )
                )
            ),
            rootType = StoringType
        )
        val lexer = lixy {
            state {
                "hello" isToken tokenOne
            }
        }
        val tokens = lexer.tokenize("hello")
        val ast = parser.parse(tokens)
        @Suppress("USELESS_IS_CHECK")
        assertTrue(ast is StoringType)
        @Suppress("USELESS_IS_CHECK")
        assertTrue(ast.child is SimpleType)
    }

    @Test
    fun `Basic parser test`() {
        val token = tokenType()
        val parser = PangoroParser(
            types = listOf(
                PangoroDescribedType(
                    type = SimpleType,
                    expectations = listOf(PangoroExpectedLixyToken(token))
                )
            ),
            rootType = SimpleType
        )
        val lexer = lixy {
            state {
                "hello" isToken token
            }
        }
        val tokens = lexer.tokenize("hello")
        val ast = parser.parse(tokens)
        @Suppress("USELESS_IS_CHECK")
        assertTrue(ast is SimpleType)
    }

    data class NumberNode(val value: String) : PangoroNode {
        companion object : PangoroNodeDeclaration<NumberNode> {
            override fun make(args: PangoroTypeDescription): NumberNode =
                NumberNode(args["value"])
        }
    }

    data class AdditionNode(val first: NumberNode, val second: NumberNode) :
        PangoroNode {
        companion object : PangoroNodeDeclaration<AdditionNode> {
            override fun make(args: PangoroTypeDescription): AdditionNode =
                AdditionNode(args["first"], args["second"])
        }
    }

    @Test
    fun `Addition parser test`() {
        val tokenNumber = tokenType()
        val tokenPlus = tokenType()
        val lexer = lixy {
            state {
                matches("\\d+") isToken tokenNumber
                "+" isToken tokenPlus
                " ".ignore
            }
        }
        val tokens = lexer.tokenize("123 + 4567")
        val parser = PangoroParser(
            listOf(
                PangoroDescribedType(
                    AdditionNode, listOf(
                        PangoroExpectedNode(NumberNode, "first"),
                        PangoroExpectedLixyToken(tokenPlus),
                        PangoroExpectedNode(NumberNode, "second")
                    )
                ),
                PangoroDescribedType(
                    NumberNode,
                    listOf(PangoroExpectedLixyToken(tokenNumber, "value"))
                )
            ),
            AdditionNode
        )
        val ast = parser.parse(tokens)
        assertEquals(AdditionNode(NumberNode("123"), NumberNode("4567")), ast)
    }
}