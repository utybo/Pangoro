package guru.zoroark.pangoro

/**
 * Class for representing the results of an expectation
 */
sealed class ExpectationResult {
    /**
     * This expectation matched successfully. It stored values in [stored]
     * and the next index that needs to be checked would be [nextIndex].
     *
     * @property stored Values that were stored as a result of this
     * expectation
     *
     * @property nextIndex The next index that needs to be checked to continue
     * the parsing process. May be out of bounds (e.g. to indicate the end of
     * the string)
     */
    class Success(val stored: Map<String, Any>, val nextIndex: Int) :
        ExpectationResult()

    /**
     * This expectation did not match.
     */
    data class DidNotMatch(
        /**
         * A human-readable reason for why this failed
         */
        val message: String,
        /**
         * The index of the token the failure happened. **This index may be out
         * of bounds.**
         */
        val atTokenIndex: Int
    ) : ExpectationResult()
}