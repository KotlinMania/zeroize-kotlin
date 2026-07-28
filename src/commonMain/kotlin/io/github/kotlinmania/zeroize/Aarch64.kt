// port-lint: source aarch64.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.zeroize

// The upstream aarch64.rs module provides Zeroize impls for ARM64 NEON
// SIMD register types (uint8x8_t, uint8x16_t, uint16x4_t, uint16x8_t,
// uint32x2_t, uint32x4_t, uint64x1_t, uint64x2_t). These are
// platform-specific intrinsic types backed by hardware NEON registers
// with no Kotlin common equivalent. Kotlin/Native does not expose ARM64
// NEON intrinsics in a cross-target way. These impls are not portable to
// Kotlin common code.
