package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.lixy.selfBuildable
import guru.zoroark.pangoro.PangoroDescribedType
import guru.zoroark.pangoro.PangoroExpectation
import guru.zoroark.pangoro.PangoroNode
import guru.zoroark.pangoro.PangoroNodeDeclaration

@PangoroDsl
class PangoroDescriptorBuilder<T : PangoroNode>(
    private val typeDeclaration: PangoroNodeDeclaration<T>
) : Buildable<PangoroDescribedType> {
    private val expectations = mutableListOf<Buildable<PangoroExpectation>>()

    operator fun plusAssign(expectation: PangoroExpectation) {
        expectations += expectation.selfBuildable()
    }

    operator fun plusAssign(expectationBuilder: Buildable<PangoroExpectation>) {
        expectations += expectationBuilder
    }

    override fun build(): PangoroDescribedType {
        return PangoroDescribedType(
            typeDeclaration,
            expectations.map { it.build() })
    }
}