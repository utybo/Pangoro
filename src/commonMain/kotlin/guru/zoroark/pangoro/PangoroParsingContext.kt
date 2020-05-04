package guru.zoroark.pangoro

import guru.zoroark.lixy.LixyToken

class PangoroParsingContext(
    val tokens: List<LixyToken>,
    val typeMap: Map<PangoroNodeDeclaration<*>, PangoroDescribedType>
)