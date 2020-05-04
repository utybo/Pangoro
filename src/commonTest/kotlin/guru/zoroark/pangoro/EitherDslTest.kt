package guru.zoroark.pangoro

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.repeated
import guru.zoroark.lixy.tokenType
import guru.zoroark.pangoro.dsl.either
import guru.zoroark.pangoro.dsl.expect
import guru.zoroark.pangoro.dsl.or
import guru.zoroark.pangoro.dsl.pangoro
import kotlin.test.Test
import kotlin.test.assertEquals

sealed class Expression : PangoroNode {
    companion object : PangoroNodeDeclaration<Expression> {
        override fun make(args: PangoroTypeDescription): Expression {
            return args["expr"]
        }
    }
}

data class Operation(
    val leftOperand: Expression,
    val operator: String,
    val rightOperand: Expression
) : Expression() {
    companion object : PangoroNodeDeclaration<Operation> {
        override fun make(args: PangoroTypeDescription): Operation =
            Operation(args["left"], args["op"], args["right"])
    }
}

data class Number(
    val value: String
) : Expression() {
    companion object : PangoroNodeDeclaration<Number> {
        override fun make(args: PangoroTypeDescription): Number =
            Number(args["value"])
    }
}

class EitherDslTest {


    @Test
    fun test_either_dsl() {
        // Operation parser

        val tOperator = tokenType("tOperator")
        val tNumber = tokenType("tNumber")
        val tOpenPar = tokenType("tOpenPar")
        val tClosePar = tokenType("tClosePar")
        val lexer = lixy {
            state {
                anyOf("+", "-", "/", "*") isToken tOperator
                ('0'..'9').repeated isToken tNumber
                "(" isToken tOpenPar
                ")" isToken tClosePar
            }
        }
        val parser = pangoro {
            Expression root {
                either {
                    expect(Number) storeIn "expr"
                } or {
                    expect(tOpenPar)
                    expect(Operation) storeIn "expr"
                    expect(tClosePar)
                }
            }
            Number {
                expect(tNumber) storeIn "value"
            }
            Operation {
                expect(Expression) storeIn "left"
                expect(tOperator) storeIn "op"
                expect(Expression) storeIn "right"
            }
        }
        val str = "((3+4)/(1+(3-1)))"
        assertEquals(
            Operation(
                Operation(
                    Number("3"),
                    "+",
                    Number("4")
                ),
                "/",
                Operation(
                    Number("1"),
                    "+",
                    Operation(
                        Number("3"),
                        "-",
                        Number("1")
                    )
                )
            ),
            parser.parse(lexer.tokenize(str))
        )
    }
}