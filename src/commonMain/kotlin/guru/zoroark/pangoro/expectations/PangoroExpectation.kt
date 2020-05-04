package guru.zoroark.pangoro.expectations

import guru.zoroark.pangoro.ExpectationResult
import guru.zoroark.pangoro.PangoroParsingContext

/**
 * General class for an expectation.
 *
 * Expectations are the core of the parsing algorithms used by Pangoro, and a
 * way to express what we *expect* the chain of tokens to look like in order to
 * be parsed into an abstract syntax tree.
 *
 * Each node of the tree is described as a list of expectations over a chain of
 * token. In other words, constructing a node is as simple as just checking if
 * the list of expectations matches the chain of tokens.
 *
 * Recognizing tokens is not just about expectations: we also need to store the
 * matched value of some expectations. Take a string for example:
 *
 *  ```
 *  "Hello World!"
 *  ```
 *
 * While the quotation marks (`"`) are not useful for the abstract syntax tree
 * (the fact that this is a string will be represented by having a node type
 * specific to string values), the content of the string is very important.
 * Therefore, expectations also know where their matched value should be stored
 * through the [storeValueIn] property. This stored value can then be retrieved
 * through the [PangoroTypeDescription]'s
 * [arguments][PangoroTypeDescription.arguments] and are passed using the [make
 * function][PangoroNodeDeclaration.make] in the [PangoroNodeDeclaration].
 */
abstract class PangoroExpectation(
    /**
     * The name of the argument where the result of this expectation should be
     * stored, or `null` if the matched value of this expectation should not be
     * stored.
     */
    val storeValueIn: String? = null
) {
    /**
     * Check if this expectation matches the given context at the given index
     * among the [context's tokens list][PangoroParsingContext.tokens].
     */
    abstract fun matches(
        context: PangoroParsingContext,
        index: Int
    ): ExpectationResult
}

/**
 * Apply each expectation of this list to the given context starting at the
 * given index.
 */
fun List<PangoroExpectation>.apply(
    context: PangoroParsingContext,
    startAt: Int = 0
): ExpectationResult {
    var index = startAt
    val map = mutableMapOf<String, Any>()
    forEach {
        if(index >= context.tokens.size) {
            return ExpectationResult.DidNotMatch(
                "Expected more tokens, but ran out of tokens",
                index
            )
        }
        val result = it.matches(context, index)
        if (result is ExpectationResult.Success) {
            map.putAll(result.stored)
            index = result.nextIndex
        } else {
            return result
        }
    }
    return ExpectationResult.Success(map, index)
}