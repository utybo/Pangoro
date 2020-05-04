package guru.zoroark.pangoro

import guru.zoroark.pangoro.expectations.PangoroExpectation

/**
 * A node type declaration (providing a way to make the actual type) with its
 * descriptor (which declares what the type expects).
 *
 * @property type The type described
 *
 * @property expectations The descriptor: currently, just a list of the
 * expectations that make up this type
 */
class PangoroDescribedType(
    val type: PangoroNodeDeclaration<*>,
    val expectations: List<PangoroExpectation>
)