package guru.zoroark.pangoro

import kotlin.test.*
import guru.zoroark.lixy.*
import guru.zoroark.lixy.matchers.repeated
import guru.zoroark.pangoro.dsl.expect
import guru.zoroark.pangoro.dsl.optional
import guru.zoroark.pangoro.dsl.pangoro


class OptionalDslTest {

    data class Decimal(val intPart: String, val decPart: String? = null) :
        PangoroNode {
        companion object : PangoroNodeDeclaration<Decimal> {
            override fun make(args: PangoroTypeDescription) =
                if (args.arguments.containsKey("decPart"))
                    Decimal(args["intPart"], args["decPart"])
                else
                    Decimal(args["intPart"])
        }
    }

    @Test
    fun test_optional_dsl() {
        val tNum = tokenType()
        val tDot = tokenType()
        val lexer = lixy {
            state {
                ('0'..'9').repeated isToken tNum
                "." isToken tDot
            }
        }
        val parser = pangoro {
            Decimal root {
                expect(tNum) storeIn "intPart"
                optional {
                    expect(tDot)
                    expect(tNum) storeIn "decPart"
                }
            }
        }
        val result = parser.parse(lexer.tokenize("123.456"))
        assertEquals(Decimal("123", "456"), result)

        val result2 = parser.parse(lexer.tokenize("789"))
        assertEquals(Decimal("789"), result2)
    }

}