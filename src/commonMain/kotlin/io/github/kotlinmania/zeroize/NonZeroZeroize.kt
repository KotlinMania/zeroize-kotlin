// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides Zeroize impls for all NonZero* integer types
// (NonZeroI8, NonZeroI16, ..., NonZeroUsize). These types zeroize to 1
// (not 0) because 0 is not a valid bit pattern for a non-zero integer.
//
// Kotlin does not have NonZero integer types in its standard library.
// The closest equivalent is non-nullable types, but they have no
// "forbidden zero value" constraint. This semantic is not portable to
// Kotlin common code.
