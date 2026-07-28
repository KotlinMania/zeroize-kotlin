// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides Zeroize impls for MaybeUninit<Z> and
// [MaybeUninit<Z>] which fill the memory with zeroes via volatile writes.
// MaybeUninit represents potentially uninitialized memory — a concept
// that does not exist in Kotlin's type system. Kotlin variables are
// always either initialized or explicitly null. This semantic is not
// portable to Kotlin common code.
