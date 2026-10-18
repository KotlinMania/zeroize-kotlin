package io.github.kotlinmania.zeroize

/**
 * Zeroizes a flat type/struct by calling its [Zeroize.zeroize] implementation
 * and issuing a memory fence.
 */
fun <F : Zeroize> zeroizeFlatType(data: F) {
    data.zeroize()
    atomicFence()
}
