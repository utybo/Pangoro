package guru.zoroark.pangoro

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
    ): ExpectationResult {
        val describedNode = context.typeMap[node]!!
        val result = describedNode.expectations.apply(context, index)
        return if (result is ExpectationResult.Success) {
            ExpectationResult.Success(
                if (storeValueIn == null) mapOf()
                else mapOf(
                    storeValueIn to describedNode.type.make(
                        PangoroTypeDescription(
                            result.stored
                        )
                    )
                ),
                result.nextIndex
            )
        } else ExpectationResult.DidNotMatch
    }
}