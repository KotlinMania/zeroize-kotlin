// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate uses a blanket impl: `impl<Z: DefaultIsZeroes> Zeroize
// for Z` which overwrites the value with its default via a volatile write.
// It also provides DefaultIsZeroes impls for all scalar types: bool, char,
// f32, f64, i8..i128, u8..u128, PhantomPinned, and the unit type.
//
// Kotlin does not have a blanket impl capability for value types in common
// code — extension functions on reified primitives would need per-type
// declarations. The zeroize extension functions on primitive arrays
// (ByteArray, IntArray, etc.) in Zeroize.kt cover the slice-level
// zeroization that the upstream DefaultIsZeroes slice impl provides.
// Individual scalar zeroization is handled by overwriting the mutable
// variable directly at the call site.
