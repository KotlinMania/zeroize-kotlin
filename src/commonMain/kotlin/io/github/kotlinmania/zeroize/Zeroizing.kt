// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.zeroize

import kotlin.native.HiddenFromObjC

/**
 * [Zeroizing] is a wrapper for any `Z: Zeroize` type which calls [zeroize] on
 * the inner value when the wrapper is [closed][close].
 *
 * The wrapper itself also implements [Zeroize] so it can participate in
 * recursive zeroization.
 */
@HiddenFromObjC
class Zeroizing<Z : Zeroize>(
    /**
     * Move [value] inside a [Zeroizing] wrapper which ensures it will be
     * zeroized when it is closed.
     */
    val value: Z,
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable {
    public companion object {
        /**
         * Move [value] inside a [Zeroizing] wrapper which ensures it will be
         * zeroized when the wrapper is closed.
         */
        public fun <Z : Zeroize> new(value: Z): Zeroizing<Z> = Zeroizing(value)

        /**
         * Convert [value] into a [Zeroizing] wrapper.
         */
        public fun <Z : Zeroize> from(value: Z): Zeroizing<Z> = Zeroizing(value)
    }

    /**
     * Return the wrapped value by reference.
     */
    public fun deref(): Z = value

    /**
     * Return the wrapped value for mutation.
     */
    public fun derefMut(): Z = value

    /**
     * Return the wrapped value by reference.
     */
    public fun asRef(): Z = value

    /**
     * Return the wrapped value for mutation.
     */
    public fun asMut(): Z = value

    override fun zeroize() {
        value.zeroize()
    }

    override fun close() {
        value.zeroize()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Zeroizing<*>) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "Zeroizing($value)"
}
