package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.pangoro.*

@PangoroDsl
class PangoroParserBuilder : Buildable<PangoroParser<*>> {
    private var rootNodeType: PangoroNodeDeclaration<*>? = null
    private val builtTypeDef = mutableListOf<PangoroDescribedType>()

    operator fun <T : PangoroNode> PangoroNodeDeclaration<T>.invoke(block: PangoroDescriptorBuilder<T>.() -> Unit) {
        builtTypeDef += PangoroDescriptorBuilder(this).apply(block).build()
    }

    infix fun <T : PangoroNode> PangoroNodeDeclaration<T>.root(block: PangoroDescriptorBuilder<T>.() -> Unit) {
        this(block)
        rootNodeType = this
    }

    override fun build(): PangoroParser<*> =
        PangoroParser(builtTypeDef, rootNodeType!!)
}

@PangoroDsl
fun pangoro(block: PangoroParserBuilder.() -> Unit): PangoroParser<*> =
    PangoroParserBuilder().apply(block).build()