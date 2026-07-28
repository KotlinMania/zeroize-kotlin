// port-lint: tests tests/zeroize_derive.rs
package io.github.kotlinmania.zeroize

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class DeriveTupleStruct(
    val arr: ByteArray,
) : Zeroize,
    ZeroizeOnDrop {
    override fun zeroize() {
        arr.zeroize()
    }
}

private class DeriveStruct(
    val string: CharArray,
    val vec: MutableList<Counter2>,
    val bytearray: ByteArray,
    var number: Int,
    var boolean: Boolean,
) : Zeroize,
    ZeroizeOnDrop {
    override fun zeroize() {
        string.zeroize()
        vec.zeroize()
        bytearray.zeroize()
        number = 0
        boolean = false
    }
}

private class DeriveStructSkip(
    val string: CharArray,
    val vec: MutableList<Counter2>,
    val bytearray: ByteArray, // skipped
    var number: Int,
    var boolean: Boolean,
) : Zeroize,
    ZeroizeOnDrop {
    override fun zeroize() {
        string.zeroize()
        vec.zeroize()
        // bytearray is skipped (upstream #[zeroize(skip)])
        number = 0
        boolean = false
    }
}

private class Counter2(
    var v: Int = 42,
) : Zeroize {
    override fun zeroize() {
        v = 0
    }
}

private class DeriveEnumValue(
    var value: Int,
) : Zeroize,
    ZeroizeOnDrop {
    override fun zeroize() {
        value = 0
    }
}

class ZeroizeDeriveTest {
    @Test
    fun deriveTupleStructTest() {
        val value = DeriveTupleStruct(byteArrayOf(1, 2, 3))
        value.zeroize()
        assertContentEquals(byteArrayOf(0, 0, 0), value.arr)
    }

    @Test
    fun deriveStructTest() {
        val value =
            DeriveStruct(
                charArrayOf('H', 'e', 'l', 'l', 'o'),
                mutableListOf(Counter2(), Counter2(), Counter2()),
                byteArrayOf(4, 5, 6),
                42,
                true,
            )
        value.zeroize()
        assertContentEquals(CharArray(5), value.string)
        assertTrue(value.vec.isEmpty())
        assertContentEquals(byteArrayOf(0, 0, 0), value.bytearray)
        assertEquals(0, value.number)
        assertFalse(value.boolean)
    }

    @Test
    fun deriveEnumTest() {
        val value = DeriveEnumValue(26)
        value.zeroize()
        assertEquals(0, value.value)
    }

    @Test
    fun deriveStructSkip() {
        val value =
            DeriveStructSkip(
                charArrayOf('H', 'e', 'l', 'l', 'o'),
                mutableListOf(Counter2(), Counter2(), Counter2()),
                byteArrayOf(4, 5, 6),
                42,
                true,
            )
        value.zeroize()
        assertContentEquals(CharArray(5), value.string)
        assertTrue(value.vec.isEmpty())
        // bytearray is skipped, should remain unchanged
        assertContentEquals(byteArrayOf(4, 5, 6), value.bytearray)
        assertEquals(0, value.number)
        assertFalse(value.boolean)
    }

    @Test
    fun deriveInheritZeroizeOnDrop() {
        // Test that a struct wrapping another ZeroizeOnDrop type
        // properly zeroizes through delegation.
        val inner = DeriveTupleStruct(byteArrayOf(1, 2, 3))
        val outer =
            object : Zeroize, ZeroizeOnDrop {
                override fun zeroize() {
                    inner.zeroize()
                }
            }
        outer.zeroize()
        assertContentEquals(byteArrayOf(0, 0, 0), inner.arr)
    }

    @Test
    fun deriveZeroizeUnusedParam() {
        // A type parameter that is not used in zeroizable fields is fine.
        class Z<T>(
            val arr: IntArray,
            @Suppress("unused") val skipped: T,
        ) : Zeroize {
            override fun zeroize() {
                arr.zeroize()
            }
        }
        val value = Z(IntArray(3) { 42 }, "unused")
        value.zeroize()
        assertContentEquals(IntArray(3), value.arr)
    }
}
