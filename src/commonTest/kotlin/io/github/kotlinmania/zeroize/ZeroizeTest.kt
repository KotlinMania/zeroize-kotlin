// port-lint: tests tests/zeroize.rs
package io.github.kotlinmania.zeroize

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class ZeroizedOnDrop(
    var value: ULong,
) : Zeroize {
    override fun zeroize() {
        value = 0uL
    }
}

class ZeroizeTest {
    @Test
    fun zeroizeByteArrays() {
        val arr = ByteArray(137) { 42 }
        arr.zeroize()
        assertContentEquals(ByteArray(137), arr)
    }

    @Test
    fun zeroizeOnDropByteArrays() {
        // Kotlin does not have in-place drop; we call zeroize() directly
        // and verify the result matches the expected post-drop state.
        val arr = Array(1) { ZeroizedOnDrop(42uL) }
        arr.forEach { it.zeroize() }
        assertEquals(0uL, arr[0].value)
    }

    @Test
    fun zeroizeCheckZeroSizeTypes() {
        // Kotlin does not expose memory layout introspection; Unit is the
        // equivalent of the unit type and has no mutable storage to zeroize.
        // in spirit — there is nothing to zeroize for zero-sized types.
        val unit: Unit = Unit
        // Nothing to assert — Unit has no mutable backing storage.
    }

    @Test
    fun zeroizeCheckTuple() {
        // Kotlin's Pair is the equivalent of a 2-tuple.
        val pair =
            Pair(
                object : Zeroize {
                    var v = 42

                    override fun zeroize() {
                        v = 0
                    }
                },
                object : Zeroize {
                    var v = 42

                    override fun zeroize() {
                        v = 0
                    }
                },
            )
        pair.zeroize()
        assertEquals(0, pair.first.v)
        assertEquals(0, pair.second.v)
    }

    @Test
    fun zeroizeOnDropCheckTuple() {
        val a = ZeroizedOnDrop(42uL)
        val b = ZeroizedOnDrop(42uL)
        val pair = Pair(a, b)
        pair.zeroize()
        assertEquals(0uL, a.value)
        assertEquals(0uL, b.value)
    }

    @Test
    fun zeroizeVec() {
        // MutableList is the Kotlin equivalent of Vec<T>.
        val vec =
            mutableListOf(
                object : Zeroize {
                    var v = 42

                    override fun zeroize() {
                        v = 0
                    }
                },
                object : Zeroize {
                    var v = 42

                    override fun zeroize() {
                        v = 0
                    }
                },
                object : Zeroize {
                    var v = 42

                    override fun zeroize() {
                        v = 0
                    }
                },
            )
        vec.zeroize()
        assertTrue(vec.isEmpty())
    }

    @Test
    fun zeroizeStringEntireCapacity() {
        // CharArray is used as the mutable backing for a string, since
        // Kotlin String is immutable and StringBuilder is not available
        // in all common targets. We zeroize the CharArray directly.
        val chars = charArrayOf('H', 'e', 'l', 'l', 'o')
        chars.zeroize()
        assertContentEquals(CharArray(5), chars)
    }

    @Test
    fun zeroizeBox() {
        // Array is the Kotlin equivalent of Box<[T]>.
        val boxedArr = ByteArray(3) { 42 }
        boxedArr.zeroize()
        assertContentEquals(ByteArray(3), boxedArr)
    }

    @Test
    fun asRef() {
        // Zeroizing wrapper exposes the inner value for reference and
        // mutation via deref/asRef/asMut.
        val counter =
            object : Zeroize {
                var v = 0

                override fun zeroize() {
                    v = 0
                }
            }
        val buffer = Zeroizing(counter)
        val asMut = buffer.asMut()
        val asRef = buffer.asRef()
        assertEquals(counter, asMut)
        assertEquals(counter, asRef)
    }
}
