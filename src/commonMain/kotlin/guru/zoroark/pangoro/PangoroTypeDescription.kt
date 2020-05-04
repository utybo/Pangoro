package guru.zoroark.pangoro

/**
 * The description for the creation of a type
 */
class PangoroTypeDescription(val arguments: Map<String, Any>) {
    inline operator fun <reified T> get(str: String): T {
        return arguments.getValue(str) as T
    }
}