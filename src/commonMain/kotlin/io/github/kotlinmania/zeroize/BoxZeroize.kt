// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides Zeroize impls for:
//   - Wrapping<Z>: delegates to inner Z
//   - Box<[Z]>: delegates to slice iter_mut
//   - Box<str>: delegates to str
//   - CString: converts to Vec, zeroizes, converts back
//
// Kotlin has no Wrapping type (arithmetic overflow is defined behavior),
// no Box type (references are not heap-pinned), and no CString type in
// common code. The Box semantics (owned heap slice that cannot
// reallocate) are covered by the MutableList and Array zeroize impls.
// These impls are not portable to Kotlin common code.
