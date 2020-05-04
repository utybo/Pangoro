package guru.zoroark.pangoro.expectations

import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.pangoro.ExpectationResult
import guru.zoroark.pangoro.PangoroParsingContext

/**
 * An expectation that expects a token to be present at this point.
 *
 * For example:
 *
 *  ```
 *  "Hello!"
 *  Tokens: [QUOTE = ", STRING_CONTENT = Hello!, QUOTE = "]
 *  ```
 *
 * You could define a "string value" node with three expectations:
 *
 * - For the first `"`, an expectation for a QUOTE token (see
 * [PangoroExpectedLixyToken])
 *
 * - For the string content in the middle, an expectation for a STRING_CONTENT
 * token.
 *
 * - For the second `"`, an expectation for a QUOTE token.
 */
class PangoroExpectedLixyToken(
    private val tokenType: LixyTokenType,
    storeValueIn: String? = null
) : PangoroExpectation(storeValueIn) {
    override fun matches(
        context: PangoroParsingContext,
        index: Int
    ): ExpectationResult = with(context) {
        val token = tokens[index]
        if (token.tokenType == tokenType)
            return ExpectationResult.Success(
                if (storeValueIn == null) mapOf()
                else mapOf(storeValueIn to token.string),
                index + 1
            )
        return ExpectationResult.DidNotMatch(
            "Expected token of type $tokenType, but encountered ${token.tokenType} instead",
            index
        )
    }
}