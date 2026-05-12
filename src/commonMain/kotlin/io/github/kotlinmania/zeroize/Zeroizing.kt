// port-lint: source src/lib.rs
package io.github.kotlinmania.zeroize

/**
 * [Zeroizing] is a wrapper for any `Z: Zeroize` type which calls [zeroize] on
 * the inner value when the wrapper is [closed][close].
 *
 * The wrapper itself also implements [Zeroize] so it can participate in
 * recursive zeroization.
 */
class Zeroizing<Z : Zeroize>
/**
 * Move [value] inside a [Zeroizing] wrapper which ensures it will be
 * zeroized when it is closed.
 */
constructor(val value: Z) : Zeroize, ZeroizeOnDrop, AutoCloseable {

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
