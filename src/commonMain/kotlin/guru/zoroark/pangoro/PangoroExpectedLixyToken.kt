package guru.zoroark.pangoro

import guru.zoroark.lixy.LixyTokenType

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
 * - For the first `"`, an expectation for a QUOTE token
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
        if (tokens[index].tokenType == tokenType)
            return ExpectationResult.Success(
                if (storeValueIn == null) mapOf()
                else mapOf(storeValueIn to token.string),
                index + 1
            )
        return ExpectationResult.DidNotMatch
    }
}