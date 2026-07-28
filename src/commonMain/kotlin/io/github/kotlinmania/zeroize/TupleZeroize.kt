// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

/**
 * Zeroize both components of a [Pair] by delegating each to its own
 * [Zeroize] implementation, mirroring the upstream tuple impls.
 */
fun <A : Zeroize, B : Zeroize> Pair<A, B>.zeroize() {
    first.zeroize()
    second.zeroize()
}

/**
 * Zeroize all three components of a [Triple] by delegating each to its
 * own [Zeroize] implementation.
 *
 * The upstream Rust crate provides tuple impls up to arity 10; Kotlin's
 * standard library only ships [Pair] and [Triple], so those are the
 * only arities ported here.
 */
fun <A : Zeroize, B : Zeroize, C : Zeroize> Triple<A, B, C>.zeroize() {
    first.zeroize()
    second.zeroize()
    third.zeroize()
}
