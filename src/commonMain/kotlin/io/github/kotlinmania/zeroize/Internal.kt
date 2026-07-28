// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides an internal module with AssertZeroizeOnDrop
// and AssertZeroize traits used as auto-deref workarounds for the
// derive proc macros. These are compile-time support types for Rust's
// proc-macro system and have no Kotlin equivalent. The Kotlin port does
// not use proc macros — users implement Zeroize and ZeroizeOnDrop
// directly. This module is not portable to Kotlin common code.
