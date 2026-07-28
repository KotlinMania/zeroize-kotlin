// port-lint: source x86.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.zeroize

// The upstream x86.rs module provides Zeroize impls for x86 SIMD register
// types (__m128, __m128d, __m128i, __m256, __m256d, __m256i, and optionally
// __m512* under the "simd" feature). These are platform-specific intrinsic
// types backed by hardware SIMD registers with no Kotlin common equivalent.
// Kotlin/Native does not expose x86 SIMD intrinsics in a cross-target way,
// and Kotlin/JVM has no equivalent either. These impls are not portable to
// Kotlin common code.
