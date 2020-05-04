package guru.zoroark.pangoro

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.tokenType
import guru.zoroark.pangoro.expectations.PangoroExpectation
import guru.zoroark.pangoro.expectations.PangoroExpectedLixyToken
import guru.zoroark.pangoro.expectations.apply
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExpectationApplyTest {
    @Test
    fun test_successful_with_no_expectations() {
        val result = listOf<PangoroExpectation>().apply(
            PangoroParsingContext(listOf(), mapOf())
        )
        assertTrue(result is ExpectationResult.Success)
        assertTrue(result.stored.isEmpty())
        assertEquals(0, result.nextIndex)
    }

    @Test
    fun test_successful_with_some_expectations_and_storage() {
        val one = tokenType()
        val two = tokenType()
        val exp = listOf<PangoroExpectation>(
            PangoroExpectedLixyToken(one, "1"),
            PangoroExpectedLixyToken(one, "2"),
            PangoroExpectedLixyToken(two, "3"),
            PangoroExpectedLixyToken(one),
            PangoroExpectedLixyToken(two, "4")
        )
        val tokens = lixy {
            state {
                'a'..'c' isToken one
                'd'..'f' isToken two
            }
        }.tokenize("abdce")
        val result = exp.apply(PangoroParsingContext(tokens, mapOf()), 0)
        assertTrue(result is ExpectationResult.Success)
        assertEquals(
            mapOf("1" to "a", "2" to "b", "3" to "d", "4" to "e"),
            result.stored
        )
    }

    @Test
    fun test_unsuccessful_out_of_tokens() {
        val one = tokenType()
        val two = tokenType()
        val exp = listOf<PangoroExpectation>(
            PangoroExpectedLixyToken(one, "1"),
            PangoroExpectedLixyToken(one, "2"),
            PangoroExpectedLixyToken(two, "3"),
            PangoroExpectedLixyToken(one),
            PangoroExpectedLixyToken(two, "4")
        )
        val tokens = lixy {
            state {
                'a'..'c' isToken one
                'd'..'f' isToken two
            }
        }.tokenize("abdc")
        val result = exp.apply(PangoroParsingContext(tokens, mapOf()))
        assertTrue(result is ExpectationResult.DidNotMatch)
        assertEquals(result.atTokenIndex, 4)
    }

    @Test
    fun test_unsuccessful_no_match() {
        val one = tokenType()
        val two = tokenType()
        val exp = listOf<PangoroExpectation>(
            PangoroExpectedLixyToken(one, "1"),
            PangoroExpectedLixyToken(one, "2"),
            PangoroExpectedLixyToken(two, "3"),
            PangoroExpectedLixyToken(one),
            PangoroExpectedLixyToken(two, "4")
        )
        val tokens = lixy {
            state {
                'a'..'c' isToken one
                'd'..'f' isToken two
            }
        }.tokenize("abdfe")
        val result = exp.apply(PangoroParsingContext(tokens, mapOf()), 0)
        assertTrue(result is ExpectationResult.DidNotMatch)
    }
}