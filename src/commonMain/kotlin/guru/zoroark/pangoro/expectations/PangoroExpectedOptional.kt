package guru.zoroark.pangoro.expectations

import guru.zoroark.pangoro.ExpectationResult
import guru.zoroark.pangoro.PangoroParsingContext

/**
 * An optional branch. This expectation is always met (always returns a
 * success), even when we have ran out of tokens.
 *
 * The returned success has an empty map if we have ran out of tokens (token
 * drought) or if the branch does not match. Otherwise, the map is the one
 * returned by the branch (so it passes directly all the matched stuff).
 */
class PangoroExpectedOptional(private val expectations: List<PangoroExpectation>) :
    PangoroExpectation(), HandlesTokenDrought {
    override fun matches(
        context: PangoroParsingContext,
        index: Int
    ): ExpectationResult =
        if (index >= context.tokens.size)
            ExpectationResult.Success(mapOf(), index)
        else when (val result = expectations.apply(context, index)) {
            is ExpectationResult.Success -> result
            is ExpectationResult.DidNotMatch -> ExpectationResult.Success(
                mapOf(),
                index
            )
        }
}