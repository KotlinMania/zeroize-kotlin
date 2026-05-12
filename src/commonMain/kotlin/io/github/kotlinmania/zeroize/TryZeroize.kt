// port-lint: source src/lib.rs
package io.github.kotlinmania.zeroize

/**
 * Fallible interface for representing cases where zeroization may or may not
 * be possible.
 *
 * This is primarily useful for scenarios like reference-counted data, where
 * zeroization is only possible when the last reference is dropped.
 */
interface TryZeroize {
    /**
     * Try to zero out this object from memory using primitives which ensure
     * the zeroization operation is not "optimized away" by the compiler.
     */
    fun tryZeroize(): Boolean
}
