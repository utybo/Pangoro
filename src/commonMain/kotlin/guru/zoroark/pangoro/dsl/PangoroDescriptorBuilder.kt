package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.lixy.selfBuildable
import guru.zoroark.pangoro.PangoroDescribedType
import guru.zoroark.pangoro.PangoroExpectation
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration

/**
 * Builder for the description of a node. This typically should be used like
 * this:
 *
 *  ```
 *  MyNodeType {
 *      expect(TokenType)
 *      expect(MyOtherNodeType)
 *  }
 *  ```
 */
@PangoroDsl
class PangoroDescriptorBuilder<T : PangoroNode>(
    private val typeDeclaration: PangoroNodeDeclaration<T>
) : Buildable<PangoroDescribedType> {
    /**
     * List of the expectations that should be built.
     */
    private val expectations = mutableListOf<Buildable<PangoroExpectation>>()

    /**
     * Add an expectation directly.
     */
    operator fun plusAssign(expectation: PangoroExpectation) {
        expectations += expectation.selfBuildable()
    }

    /**
     * Add an expectation that will be built when the description gets built.
     */
    operator fun plusAssign(expectationBuilder: Buildable<PangoroExpectation>) {
        expectations += expectationBuilder
    }

    /**
     * Builds this as a described type
     */
    override fun build(): PangoroDescribedType {
        return PangoroDescribedType(
            typeDeclaration,
            expectations.map { it.build() })
    }
}