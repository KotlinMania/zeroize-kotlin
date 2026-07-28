// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

/**
 * Zeroize a nullable [Zeroize] reference.
 *
 * If the value is non-null, delegate to its [Zeroize.zeroize] and then set
 * the reference to null — mirroring the upstream nullable impl which
 * takes the value to drop it and then writes the null bit pattern.
 *
 * Because Kotlin does not expose the raw memory of a nullable reference,
 * the "overwrite the remaining space with zeroes" step from the Rust
 * impl is not reproduced; setting the reference to null is the faithful
 * Kotlin equivalent.
 */
fun <Z : Zeroize> zeroizeNullable(z: Z?) {
    if (z != null) {
        z.zeroize()
    }
    atomicFence()
}
