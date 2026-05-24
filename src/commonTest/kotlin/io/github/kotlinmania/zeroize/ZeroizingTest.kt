// port-lint: source tests/zeroize.rs
package io.github.kotlinmania.zeroize

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertSame
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
        val wrapper = Zeroizing.new(inner)
        assertSame(inner, wrapper.value)
    }

    @Test
    fun fromExposesValue() {
        val inner = Counter()
        val wrapper = Zeroizing.from(inner)
        assertSame(inner, wrapper.value)
    }

    @Test
    fun referenceAccessorsExposeValue() {
        val inner = Counter()
        val wrapper = Zeroizing(inner)

        assertSame(inner, wrapper.deref())
        assertSame(inner, wrapper.derefMut())
        assertSame(inner, wrapper.asRef())
        assertSame(inner, wrapper.asMut())
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

    @Test
    fun zeroizeByteArrays() {
        val arr = ByteArray(137) { 42 }
        arr.zeroize()
        assertContentEquals(ByteArray(137), arr)
    }

    @Test
    fun zeroizeNumericArrays() {
        val shorts = ShortArray(3) { 42 }
        val ints = IntArray(3) { 42 }
        val longs = LongArray(3) { 42L }
        val floats = FloatArray(3) { 42.0f }
        val doubles = DoubleArray(3) { 42.0 }

        shorts.zeroize()
        ints.zeroize()
        longs.zeroize()
        floats.zeroize()
        doubles.zeroize()

        assertContentEquals(ShortArray(3), shorts)
        assertContentEquals(IntArray(3), ints)
        assertContentEquals(LongArray(3), longs)
        assertContentEquals(FloatArray(3), floats)
        assertContentEquals(DoubleArray(3), doubles)
    }

    @Test
    fun zeroizeTextAndBooleanArrays() {
        val chars = charArrayOf('s', 'e', 'c', 'r', 'e', 't')
        val booleans = BooleanArray(3) { true }

        chars.zeroize()
        booleans.zeroize()

        assertContentEquals(CharArray(6), chars)
        assertContentEquals(BooleanArray(3), booleans)
    }

    @Test
    fun zeroizeObjectArrays() {
        val arr = arrayOf(Counter(), Counter())
        arr.zeroize()
        assertEquals(listOf(1, 1), arr.map { it.zeroizeCalls })
    }

    @Test
    fun zeroizeMutableListClearsElementsAndList() {
        val first = Counter()
        val second = Counter()
        val list = mutableListOf(first, second)

        list.zeroize()

        assertTrue(list.isEmpty())
        assertEquals(1, first.zeroizeCalls)
        assertEquals(1, second.zeroizeCalls)
    }
}
