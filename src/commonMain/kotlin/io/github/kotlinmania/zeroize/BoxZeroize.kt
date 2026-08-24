// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides zeroization implementations for wrapping arithmetic types,
// heap-allocated slices, boxed strings, and native null-terminated strings.
//
// In Kotlin, arithmetic overflow semantics are defined on primitive types, heap allocations
// are garbage-collected references, and slice-level zeroization is supported directly
// via Array and MutableList extensions in Zeroize.kt. Additional specialized wrapper
// implementations are omitted in Kotlin common code.

