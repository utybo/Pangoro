package guru.zoroark.pangoro

import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.matches
import guru.zoroark.lixy.tokenType
import guru.zoroark.pangoro.expectations.PangoroEitherBranch
import guru.zoroark.pangoro.expectations.PangoroExpectedEither
import guru.zoroark.pangoro.expectations.PangoroExpectedLixyToken
import guru.zoroark.pangoro.expectations.PangoroExpectedNode
import kotlin.test.*

class ExpectedEitherTest {

    sealed class BranchOneOrTwo : PangoroNode {
        data class BranchOne(val oneWord: String) : BranchOneOrTwo() {
            companion object : PangoroNodeDeclaration<BranchOne> {
                override fun make(args: PangoroTypeDescription) =
                    BranchOne(args["oneWord"])
            }
        }

        data class BranchTwo(val twoWord: String) : BranchOneOrTwo() {
            companion object : PangoroNodeDeclaration<BranchTwo> {
                override fun make(args: PangoroTypeDescription): BranchTwo =
                    BranchTwo(args["twoWord"])
            }
        }
    }

    @Test
    fun either_matches_first_branch() {
        val str = "test (hello 1)"
        val tWord = tokenType("tWord")
        val tPar = tokenType("tPar")
        val tOne = tokenType("tOne")
        val tTwo = tokenType("tTwo")
        val lexer = lixy {
            state {
                matches("1") isToken tOne
                matches("2") isToken tTwo
                matches("\\w+") isToken tWord
                anyOf("(", ")") isToken tPar
                " ".ignore
            }
        }
        val pee = PangoroExpectedEither(
            listOf(
                PangoroEitherBranch(
                    listOf(
                        PangoroExpectedLixyToken(tPar),
                        PangoroExpectedNode(
                            BranchOneOrTwo.BranchOne,
                            "ambiguous"
                        ),
                        PangoroExpectedLixyToken(tPar)
                    )
                ),
                PangoroEitherBranch(
                    listOf(
                        PangoroExpectedLixyToken(tPar),
                        PangoroExpectedNode(
                            BranchOneOrTwo.BranchTwo,
                            "ambiguous"
                        ),
                        PangoroExpectedLixyToken(tPar)
                    )
                )
            )
        )
        val result = pee.matches(
            PangoroParsingContext(
                lexer.tokenize(str),
                mapOf(
                    BranchOneOrTwo.BranchOne to PangoroDescribedType(
                        BranchOneOrTwo.BranchOne,
                        listOf(
                            PangoroExpectedLixyToken(tWord, "oneWord"),
                            PangoroExpectedLixyToken(tOne)
                        )
                    ),
                    BranchOneOrTwo.BranchTwo to PangoroDescribedType(
                        BranchOneOrTwo.BranchTwo,
                        listOf(
                            PangoroExpectedLixyToken(tWord, "twoWord"),
                            PangoroExpectedLixyToken(tTwo)
                        )
                    )
                )
            ),
            1
        )
        assertTrue(result is ExpectationResult.Success)
        assertEquals(
            BranchOneOrTwo.BranchOne("hello"),
            result.stored["ambiguous"]
        )
    }

    @Test
    fun either_matches_second_branch() {
        val str = "test (hey 2)"
        val tWord = tokenType("tWord")
        val tPar = tokenType("tPar")
        val tOne = tokenType("tOne")
        val tTwo = tokenType("tTwo")
        val lexer = lixy {
            state {
                matches("1") isToken tOne
                matches("2") isToken tTwo
                matches("\\w+") isToken tWord
                anyOf("(", ")") isToken tPar
                " ".ignore
            }
        }
        val pee = PangoroExpectedEither(
            listOf(
                PangoroEitherBranch(
                    listOf(
                        PangoroExpectedLixyToken(tPar),
                        PangoroExpectedNode(
                            BranchOneOrTwo.BranchOne,
                            "ambiguous"
                        ),
                        PangoroExpectedLixyToken(tPar)
                    )
                ),
                PangoroEitherBranch(
                    listOf(
                        PangoroExpectedLixyToken(tPar),
                        PangoroExpectedNode(
                            BranchOneOrTwo.BranchTwo,
                            "ambiguous"
                        ),
                        PangoroExpectedLixyToken(tPar)
                    )
                )
            )
        )
        val result = pee.matches(
            PangoroParsingContext(
                lexer.tokenize(str),
                mapOf(
                    BranchOneOrTwo.BranchOne to PangoroDescribedType(
                        BranchOneOrTwo.BranchOne,
                        listOf(
                            PangoroExpectedLixyToken(tWord, "oneWord"),
                            PangoroExpectedLixyToken(tOne)
                        )
                    ),
                    BranchOneOrTwo.BranchTwo to PangoroDescribedType(
                        BranchOneOrTwo.BranchTwo,
                        listOf(
                            PangoroExpectedLixyToken(tWord, "twoWord"),
                            PangoroExpectedLixyToken(tTwo)
                        )
                    )
                )
            ),
            1
        )
        assertTrue(result is ExpectationResult.Success)
        assertEquals(
            BranchOneOrTwo.BranchTwo("hey"),
            result.stored["ambiguous"]
        )
    }
}