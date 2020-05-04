package guru.zoroark.pangoro

import guru.zoroark.lixy.LixyToken

/**
 * This object contains the information that is passed to expectations, and is
 * global over a single parser run.
 *
 * @property tokens The list of tokens that should be parsed
 *
 * @property typeMap A map with all of the known declared types and their
 * description
 */
class PangoroParsingContext(
    val tokens: List<LixyToken>,
    val typeMap: Map<PangoroNodeDeclaration<*>, PangoroDescribedType>
)