package guru.zoroark.pangoro

import guru.zoroark.lixy.LixyToken

/**
 * Class for a full parser that is ready to parse a chain of tokens.
 *
 * @param R The type of the *root node*, the node at the root of the abstract
 * syntax tree
 *
 * @param types List of described types that will be used for the parsing
 * process. Each entry describes a type of node that can be encountered in the
 * AST.
 *
 * @param rootType The expected type of the root node.
 */
class PangoroParser<R : PangoroNode>(
    types: List<PangoroDescribedType>,
    val rootType: PangoroNodeDeclaration<R>
) {
    private val typeMap: Map<PangoroNodeDeclaration<*>, PangoroDescribedType> =
        types.associateBy { it.type }

    fun parse(lixyTokens: List<LixyToken>): R {
        val result = typeMap[rootType]!!.expectations.apply(
            PangoroParsingContext(lixyTokens, typeMap)
        )
        if (result is ExpectationResult.Success)
            return rootType.make(PangoroTypeDescription(result.stored))
        else
            error("Oops")
    }
}
