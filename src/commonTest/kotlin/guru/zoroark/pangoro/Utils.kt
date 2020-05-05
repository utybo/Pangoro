package guru.zoroark.pangoro

/**
 * Runs [String.contains] on the JVM, returns true on JS.
 *
 * This is because the reflection system is limited on JS and we can't check for
 * some things like class qualified name being returned.
 */
expect fun String.containsJvm(substring: String): Boolean