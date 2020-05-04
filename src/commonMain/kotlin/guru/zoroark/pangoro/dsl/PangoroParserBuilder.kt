package guru.zoroark.pangoro.dsl

import guru.zoroark.lixy.Buildable
import guru.zoroark.pangoro.*

/**
 * DSL builder for a Pangoro parser.
 */
@PangoroDsl
class PangoroParserBuilder : Buildable<PangoroParser<*>> {
    private var rootNodeType: PangoroNodeDeclaration<*>? = null
    private val builtTypeDef = mutableListOf<PangoroDescribedType>()

    /**
     * Creates a node declaration for the given node type with the given block
     * as its descriptor.
     */
    operator fun <T : PangoroNode> PangoroNodeDeclaration<T>.invoke(block: PangoroDescriptorBuilder<T>.() -> Unit) {
        // TODO Check if a type def was already built for the given type
        builtTypeDef += PangoroDescriptorBuilder(this).apply(block).build()
    }

    /**
     * Similar to [invoke], but also signals that this node is the *root node*
     * of the constructed tree.
     */
    infix fun <T : PangoroNode> PangoroNodeDeclaration<T>.root(block: PangoroDescriptorBuilder<T>.() -> Unit) {
        this(block)
        // TODO Check if a root node was already there, if yes throw exception
        rootNodeType = this
    }

    /**
     * Build this parser
     */
    override fun build(): PangoroParser<*> =
        // TODO throw a nicer exception when rootNodeType == null reminding the
        //  user to configure the root node.
        PangoroParser(builtTypeDef, rootNodeType!!)
}

/**
 * Main entry-point for the Pangoro DSL. A parser might typically look like this
 *
 *  ```
 *  val parser = pangoro {
 *      MyNodeType {
 *          expect(tokenType)
 *          expect(otherTokenType) storeIn "hello"
 *      }
 *
 *      MyOtherNodeType root {
 *          expect(someToken)
 *          expect(MyNodeType) storeIn "theNode"
 *      }
 *  }
 *  parser.parse(tokens)
 *  ```
 */
@PangoroDsl
fun pangoro(block: PangoroParserBuilder.() -> Unit): PangoroParser<*> =
    PangoroParserBuilder().apply(block).build()