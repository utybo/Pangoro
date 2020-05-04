package guru.zoroark.pangoro

import guru.zoroark.pangoro.ExpectationResult.DidNotMatch
import guru.zoroark.pangoro.ExpectationResult.Success

/**
 * An expectation that expects another node to be present at this point.
 *
 * For example:
 *
 *  ```
 *      x = (1 + 2) + 3
 *  ```
 *
 * You could define an "assignment" node with three expectations:
 *
 * - For the first operand and the equals sign, a simple
 * [PangoroExpectedLixyToken]
 *
 * - For the right operand,  a `PangoroExpectedNode(ExpressionNode)`.
 *
 * This expectation is the main way of composing nodes.
 */
class PangoroExpectedNode(
    private val node: PangoroNodeDeclaration<*>,
    storeValueIn: String? = null
) : PangoroExpectation(storeValueIn) {
    override fun matches(
        context: PangoroParsingContext,
        index: Int
    ): ExpectationResult = context.typeMap[node]?.let { describedNode ->
        when (val result = describedNode.expectations.apply(context, index)) {
            is Success ->
                Success(storeValueIn
                    ?.to(
                        describedNode.type.make(
                            PangoroTypeDescription(result.stored)
                        )
                    )
                    ?.let { mapOf(it) }
                    ?: mapOf(), result.nextIndex
                )
            is DidNotMatch -> result
        }
    } ?: throw PangoroException(
        "Node ${node::class.qualifiedName} is expected but not declared in the parser"
    )
}