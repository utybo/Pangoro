package guru.zoroark.pangoro

import guru.zoroark.lixy.LixyToken
import guru.zoroark.pangoro.expectations.PangoroExpectation
import guru.zoroark.pangoro.expectations.PangoroExpectedNode

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
    private val rootType: PangoroNodeDeclaration<R>
) {
    private val rootExpectation: PangoroExpectation =
        PangoroExpectedNode(rootType, "root")

    private val typeMap: Map<PangoroNodeDeclaration<*>, PangoroDescribedType> =
        types.associateBy { it.type }

    /**
     * Launch the parser on the given tokens.
     */
    fun parse(lixyTokens: List<LixyToken>): R {
        val result = rootExpectation.matches(
            PangoroParsingContext(lixyTokens, typeMap),
            0
        )
        @Suppress("UNCHECKED_CAST")
        return when (result) {
            is ExpectationResult.DidNotMatch ->
                throw PangoroException("Parsing failed: ${result.message} (token nb ${result.atTokenIndex})")
            is ExpectationResult.Success ->
                result.stored["root"] as? R
                    ?: error("Internal Pangoro error: the root result was not stored. Please report this.")
        }
    }
}
