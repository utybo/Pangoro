package guru.zoroark.pangoro

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches
import guru.zoroark.lixy.tokenType
import guru.zoroark.pangoro.dsl.expect
import guru.zoroark.pangoro.dsl.pangoro
import kotlin.test.*

class DslTest {

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
    fun `Addition parser DSL test`() {
        val tokenNumber = tokenType()
        val tokenPlus = tokenType()
        val lexer = lixy {
            state {
                matches("\\d+") isToken tokenNumber
                "+" isToken tokenPlus
                " ".ignore
            }
        }
        val parser = pangoro {
            NumberNode {
                expect(tokenNumber) storeIn "value"
            }
            AdditionNode root {
                expect(NumberNode) storeIn "first"
                expect(tokenPlus)
                expect(NumberNode) storeIn "second"
            }
        }
        val tokens = lexer.tokenize("123 + 4567")
        val ast = parser.parse(tokens)
        assertEquals(
            AdditionNode(
                NumberNode("123"),
                NumberNode("4567")
            ), ast
        )
    }
}