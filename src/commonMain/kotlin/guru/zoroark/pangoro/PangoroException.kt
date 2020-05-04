package guru.zoroark.pangoro

/**
 * Exception type for anything thrown by Pangoro.
 *
 * Note that individual expectations should not throw Pangoro exceptions when
 * they do not match: instead they should return a
 * [ExpectationResult.DidNotMatch] object. A `PangoroException` being thrown
 * indicates that something is very wrong, for example that the parser is
 * not configured properly.
 */
class PangoroException(message: String, cause: Throwable? = null) :
    Exception(message, cause)