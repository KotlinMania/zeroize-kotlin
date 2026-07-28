// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.zeroize

import kotlin.native.HiddenFromObjC

/**
 * Marker interface for types whose default value is the desired zeroization
 * result.
 *
 * Implementors must expose a zero value via the [defaultZero] property; the
 * generic [Zeroize] extension for [DefaultIsZeroes] overwrites the receiver's
 * mutable storage with that value.
 */
@HiddenFromObjC
interface DefaultIsZeroes<T : DefaultIsZeroes<T>> {
    /** The value used to overwrite this type when zeroizing. */
    val defaultZero: T
}

/**
 * Zeroize an array whose element type implements [DefaultIsZeroes] by
 * overwriting every element with the default zero value, then issuing a
 * memory fence.
 *
 * This mirrors the upstream slice impl for DefaultIsZeroes which fills the
 * slice with the default value via a volatile memset.
 */
@HiddenFromObjC
fun <T : DefaultIsZeroes<T>> Array<T>.zeroizeDefaultIsZeroes() {
    for (i in indices) {
        this[i] = this[i].defaultZero
    }
    atomicFence()
}
