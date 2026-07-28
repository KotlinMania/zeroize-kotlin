// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides a `zeroize_flat_type` function which
// performs a volatile memset of a flat struct to zeroes. It requires
// the type to be Sized, contain no references, no Drop impls, and have
// a valid all-zeroes bit pattern.
//
// This is an unsafe API that operates on raw pointers and relies on
// memory layout guarantees that Kotlin does not expose. Kotlin does not
// have raw pointer arithmetic or layout-based volatile memset in common
// code. This function is not portable to Kotlin common code.
