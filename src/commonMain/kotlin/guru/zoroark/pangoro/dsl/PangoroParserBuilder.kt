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
    private val knownDeclarations = mutableSetOf<PangoroNodeDeclaration<*>>()

    /**
     * Creates a node declaration for the given node type with the given block
     * as its descriptor.
     */
    operator fun <T : PangoroNode> PangoroNodeDeclaration<T>.invoke(block: PangoroDescriptorBuilder<T>.() -> Unit) {
        if (this in knownDeclarations) {
            throw PangoroException("The node declaration ${this::class.qualifiedName} was already described elsewhere: you cannot describe it twice.")
        }
        builtTypeDef += PangoroDescriptorBuilder(this).apply(block).build()
        knownDeclarations += this
    }

    /**
     * Similar to [invoke], but also signals that this node is the *root node*
     * of the constructed tree.
     */
    infix fun <T : PangoroNode> PangoroNodeDeclaration<T>.root(block: PangoroDescriptorBuilder<T>.() -> Unit) {
        if (rootNodeType != null) {
            throw PangoroException("Another node was already defined as the root, ${this::class.qualifiedName} cannot also be a root.")
        }
        this(block)
        rootNodeType = this
    }

    /**
     * Build this parser
     */
    override fun build(): PangoroParser<*> =
        //  user to configure the root node.
        PangoroParser(
            builtTypeDef,
            rootNodeType
                ?: throw PangoroException("You never defined a root node: please described a node with 'root' so the parser knows where to start!")
        )
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