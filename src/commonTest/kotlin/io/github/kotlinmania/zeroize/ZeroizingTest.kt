// port-lint: source tests/zeroize.rs
package io.github.kotlinmania.zeroize

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class Counter(var zeroizeCalls: Int = 0) : Zeroize {
    override fun zeroize() {
        zeroizeCalls += 1
    }
}

class ZeroizingTest {
    @Test
    fun newExposesValue() {
        val inner = Counter()
        val wrapper = Zeroizing(inner)
        assertEquals(inner, wrapper.value)
    }

    @Test
    fun zeroizeDelegatesToInner() {
        val inner = Counter()
        val wrapper = Zeroizing(inner)
        wrapper.zeroize()
        assertEquals(1, inner.zeroizeCalls)
    }

    @Test
    fun closeZeroizesInner() {
        val inner = Counter()
        Zeroizing(inner).use {
            assertEquals(0, inner.zeroizeCalls)
        }
        assertEquals(1, inner.zeroizeCalls)
    }

    @Test
    fun equalsAndHashCodeMatchInner() {
        val a = Counter()
        val b = Counter()
        assertEquals(Zeroizing(a), Zeroizing(a))
        assertTrue(Zeroizing(a) != Zeroizing(b))
        assertEquals(Zeroizing(a).hashCode(), a.hashCode())
    }

    @Test
    fun isZeroizeOnDrop() {
        val wrapper: ZeroizeOnDrop = Zeroizing(Counter())
        assertTrue(wrapper is Zeroize)
    }
}
