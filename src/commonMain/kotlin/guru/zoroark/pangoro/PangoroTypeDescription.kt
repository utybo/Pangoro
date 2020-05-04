package guru.zoroark.pangoro

/**
 * The description for the creation of a type
 */
class PangoroTypeDescription(
    /**
     * The arguments: that is, everything that was expected and stored using
     * the `storeIn`/`storeValueIn` constructs.
     */
    val arguments: Map<String, Any>
) {
    /**
     * Retrieve the given argument, casting it to `T` automatically
     */
    inline operator fun <reified T> get(str: String): T {
        val value = arguments.getOrElse(str) {
            throw PangoroException("Key '$str' does not exist in the stored arguments")
        }
        if (value is T) {
            return value // Auto-cast by Kotlin
        } else {
            throw PangoroException("Expected $str to be of type ${T::class.qualifiedName}, but it is actually of type ${value::class.qualifiedName}")
        }
    }
}