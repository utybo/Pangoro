package guru.zoroark.pangoro.expectations

import guru.zoroark.pangoro.ExpectationResult
import guru.zoroark.pangoro.PangoroParsingContext

/**
 * An expectation that takes the result of some [branches][PangoroEitherBranch].
 *
 * The expectation returns the result of the first successful branch, or a
 * DidNotMatch with a message that contains all of the DidNotMatch messages of
 * the branches.
 *
 * Branches are checked in the list's order.
 *
 * @property branches The branches of this expectation. They are always
 * checked in the list's order.
 */
class PangoroExpectedEither(private val branches: List<PangoroEitherBranch>) :
    PangoroExpectation() {
    override fun matches(
        context: PangoroParsingContext,
        index: Int
    ): ExpectationResult {
        val failures = mutableListOf<ExpectationResult.DidNotMatch>()
        branches.forEach {
            when (val result = it.expectations.apply(context, index)) {
                is ExpectationResult.Success -> {
                    return result
                }
                is ExpectationResult.DidNotMatch -> {
                    failures += result
                }
            }
        }
        return ExpectationResult.DidNotMatch(
            "None of the ${branches.size} branches matched.\n" +
                    failures.mapIndexed { i, x ->
                        "    -> Branch $i:\n${x.message.prependIndent("        ")}\n"
                    },
            index
        )
    }
}

/**
 * A branch for the [PangoroExpectedEither] expectation.
 *
 * @property expectations The expectations that are a part of this branch.
 */
class PangoroEitherBranch(val expectations: List<PangoroExpectation>)