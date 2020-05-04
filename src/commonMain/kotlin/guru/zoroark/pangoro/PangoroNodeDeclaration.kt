package guru.zoroark.pangoro

/**
 * A *node declaration*. This simple interface provides a way to turn the
 * description of a node into an actual node.
 *
 * You would typically implement it like so:
 *
 *  ```
 *  class MyNodeType(val child: MyOtherNodeType) : PangoroNode {
 *      companion object : PangoroNodeDeclaration<MyNodeType> {
 *          override fun make(args: PangoroTypeDescription) =
 *              MyNodeType(args["child"])
 *      }
 *  }
 *  ```
 *
 *  @see make
 */
interface PangoroNodeDeclaration<T : PangoroNode> {
    /**
     * This function creates a node of type [T] from the arguments.
     *
     * The arguments contain everything that needed to be stored for the
     * creation of the node. If you stored something in the descriptor for this
     * node, it will be available in [args].
     *
     * @args The description of the node, which contains all of the stored
     * information for creating it.
     */
    fun make(args: PangoroTypeDescription): T
}