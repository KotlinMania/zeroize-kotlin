// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

/**
 * Zeroize a [StringBuilder] by overwriting every character with a NUL and
 * then truncating it to zero length, mirroring the upstream growable-buffer
 * behavior for [String].
 *
 * Kotlin [String] is immutable — there is no way to obtain mutable access
 * to its backing storage the way Rust's String does.  Use a
 * [StringBuilder] whenever the backing buffer must be reliably zeroed.
 */
fun StringBuilder.zeroize() {
    for (i in 0 until this.length) {
        this[i] = '\u0000'
    }
    clear()
    atomicFence()
}
