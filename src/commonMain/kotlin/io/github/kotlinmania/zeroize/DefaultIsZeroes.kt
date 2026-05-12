// port-lint: source src/lib.rs
package io.github.kotlinmania.zeroize

/**
 * Marker interface for types whose default value is the desired zeroization
 * result.
 *
 * Implementors must expose a zero value via the [defaultZero] property; the
 * generic [Zeroize] extension for [DefaultIsZeroes] overwrites the receiver's
 * mutable storage with that value.
 */
interface DefaultIsZeroes<T : DefaultIsZeroes<T>> {
    /** The value used to overwrite this type when zeroizing. */
    val defaultZero: T
}
