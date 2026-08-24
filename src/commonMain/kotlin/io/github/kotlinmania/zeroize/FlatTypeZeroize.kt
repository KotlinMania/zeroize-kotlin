// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides a raw memory zeroing function for flat struct layouts.
// It performs direct volatile byte clearing across an entire struct memory buffer.
//
// In Kotlin, objects are managed on the managed heap without fixed C-style memory layouts
// in multiplatform common code. Direct layout-level volatile byte setting is not portable
// to Kotlin common code; types instead implement the Zeroize interface directly.

