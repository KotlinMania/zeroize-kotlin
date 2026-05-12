// port-lint: source src/lib.rs
package io.github.kotlinmania.zeroize

/**
 * Securely erase values from memory with a simple interface ([Zeroize]) built
 * on primitives which guarantee the operation will not be "optimized away".
 *
 * ## About
 *
 * Zeroing memory securely is hard — compilers optimize for performance, and in
 * doing so they love to "optimize away" unnecessary zeroing calls. There are
 * many documented "tricks" to attempt to avoid these optimizations and ensure
 * that a zeroing routine is performed reliably.
 *
 * This library isn't about tricks: it uses volatile writes and memory fences
 * to provide easy-to-use, portable zeroing behavior for mutable buffers and
 * wrapper types.
 *
 * - No insecure fallbacks.
 * - No dependencies.
 *
 * ## Usage
 *
 * ```
 * // Protip: don't embed secrets in your source code. This is just an example.
 * val secret = "Air shield password: 1,2,3,4,5".encodeToByteArray()
 * // [ ... ] open the air shield here
 *
 * // Now that we're done using the secret, zero it out.
 * secret.zeroize()
 * ```
 *
 * The [Zeroize] interface is implemented for mutable buffers via extension
 * functions, and for any type whose [Default]-like zero value is known via
 * [DefaultIsZeroes].
 *
 * ## [Zeroizing]: wrapper for zeroizing arbitrary values on cleanup
 *
 * [Zeroizing] is a generic wrapper type that exposes an inner value of type
 * `Z` and implements [AutoCloseable.close] by calling [zeroize] on its
 * contents:
 *
 * ```
 * fun useSecret() {
 *     Zeroizing(ByteArray(5)).use { secret ->
 *         // Set the air shield password
 *         // Protip (again): don't embed secrets in your source code.
 *         intArrayOf(1, 2, 3, 4, 5).forEachIndexed { i, b -> secret.value[i] = b.toByte() }
 *
 *         // The contents of `secret` will be automatically zeroized on close.
 *     }
 * }
 * ```
 *
 * ## What guarantees does this library provide?
 *
 * This library guarantees the following:
 *
 * 1. The zeroing operation can't be "optimized away" by the compiler.
 * 2. All subsequent reads to memory will see "zeroized" values.
 *
 * That said, there is still potential for microarchitectural attacks (ala
 * Spectre/Meltdown) to leak "zeroized" secrets through covert channels. This
 * library makes no guarantees that zeroized values cannot be leaked through
 * such channels, as they represent flaws in the underlying hardware.
 *
 * ## Stack/Heap Zeroing Notes
 *
 * This library can be used to zero values from either the stack or the heap.
 *
 * However, be aware several operations can unintentionally leave copies of
 * data in memory. This includes but is not limited to:
 *
 * - Copies introduced by value semantics
 * - Heap reallocation when using growable collections
 * - Borrowers of a reference making copies of the data
 *
 * The [Zeroize] implementations for growable collections zeroize the entire
 * capacity of their backing buffer, but cannot guarantee copies of the data
 * were not previously made by buffer reallocation. It's therefore important
 * when attempting to zeroize such buffers to initialize them to the correct
 * capacity, and take care to prevent subsequent reallocation.
 *
 * ## What about: clearing registers, mlock, mprotect, etc.?
 *
 * This library is focused on providing simple, unobtrusive support for
 * reliably zeroing memory using the best approach possible.
 *
 * Clearing registers is a difficult problem that can't easily be solved by
 * something like a library, and requires either inline assembly or compiler
 * support.
 *
 * Other memory protection mechanisms are interesting and useful, but often
 * overkill (e.g. defending against RAM scraping or attackers with swap
 * access). Such protections are explicitly out-of-scope for this library.
 *
 * Zeroing memory is good cryptographic hygiene and this library seeks to
 * promote it in the most unobtrusive manner possible.
 */
interface Zeroize {
    /**
     * Zero out this object from memory using primitives which ensure the
     * zeroization operation is not "optimized away" by the compiler.
     */
    fun zeroize()
}
