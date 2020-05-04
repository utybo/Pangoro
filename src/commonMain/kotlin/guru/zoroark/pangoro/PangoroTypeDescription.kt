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
        // TODO throw a nicer exception if the cast cannot happen
        return arguments.getValue(str) as T
    }
}