package guru.zoroark.pangoro

actual fun String.containsJvm(substring: String): Boolean {
    return contains(substring)
}