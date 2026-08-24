// port-lint: source aarch64.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.zeroize

// The upstream architecture module provides zeroization implementations for 64-bit and 128-bit
// ARM NEON SIMD vector register types. These represent hardware-specific vector registers
// without multiplatform common representation. Hardware vector register clearing is not portable
// to Kotlin common code.

