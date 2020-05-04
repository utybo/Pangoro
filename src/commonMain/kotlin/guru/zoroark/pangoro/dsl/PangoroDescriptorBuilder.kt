package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.lixy.selfBuildable
import guru.zoroark.pangoro.PangoroDescribedType
import guru.zoroark.pangoro.expectations.PangoroExpectation
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
) : ExpectationReceiver, Buildable<PangoroDescribedType> {
    /**
     * List of the expectations that should be built.
     */
    private val expectations = mutableListOf<Buildable<PangoroExpectation>>()

    /**
     * Add an expectation that will be built when the description gets built.
     */
    override operator fun plusAssign(expectationBuilder: Buildable<PangoroExpectation>) {
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

/**
 * An expectation receiver is the receiver type for all `expect` DSL constructs.
 * Use this if you want your own DSL to be able to have `expect` called on it.
 */
@PangoroDsl
interface ExpectationReceiver {
    /**
     * Add a buildable expectation to this receiver -- the exact meaning of this
     * depends on the implementation
     */
    operator fun plusAssign(expectationBuilder: Buildable<PangoroExpectation>)
}

/**
 * Add an expectation directly instead of a builder. This is a shortcut for
 * `this += expectation.selfBuildable()`
 */
operator fun ExpectationReceiver.plusAssign(expectation: PangoroExpectation) {
    this += expectation.selfBuildable()
}