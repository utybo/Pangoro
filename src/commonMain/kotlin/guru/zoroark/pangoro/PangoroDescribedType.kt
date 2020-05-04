package guru.zoroark.pangoro

/**
 * A node type declaration (providing a way to make the actual type) with its
 * descriptor (which declares what the type expects).
 */
class PangoroDescribedType(
    val type: PangoroNodeDeclaration<*>,
    val expectations: List<PangoroExpectation>
)