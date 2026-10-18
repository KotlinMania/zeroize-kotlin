package io.github.kotlinmania.zeroize

/**
 * Internal support interface for zeroize-on-drop assertions.
 */
interface AssertZeroizeOnDrop {
    /** Zeroize or invoke drop handler. */
    fun zeroizeOrOnDrop()
}

/**
 * Internal support interface for zeroize assertions.
 */
interface AssertZeroize {
    /** Zeroize or invoke drop handler. */
    fun zeroizeOrOnDrop()
}
