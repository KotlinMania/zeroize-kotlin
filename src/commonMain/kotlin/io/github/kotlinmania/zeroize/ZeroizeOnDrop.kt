// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

/**
 * Marker interface signifying that this type will [Zeroize.zeroize] itself on
 * cleanup.
 */
interface ZeroizeOnDrop
