// port-lint: tests tests/zeroize.rs
package io.github.kotlinmania.zeroize

import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.de.Visitor
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

private class Counter(
    var zeroizeCalls: Int = 0,
) : Zeroize {
    override fun zeroize() {
        zeroizeCalls += 1
    }
}

private class SerializableSecret(
    var secret: String,
) : Zeroize,
    Serialize {
    override fun zeroize() {
        secret = ""
    }

    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> =
        serializer.serializeStr(secret)
}

private object SerializableSecretDeserialize : Deserialize<SerializableSecret> {
    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<SerializableSecret> =
        SerdeResult.success(SerializableSecret("deserialized-secret"))
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

    @Test
    fun cloneCreatesEqualWrapper() {
        val secret = SerializableSecret("pa55w0rd")
        val wrapper = Zeroizing(secret)
        val cloned = wrapper.clone()
        assertEquals(wrapper, cloned)
    }

    @Test
    fun cloneFromZeroizesCurrentValue() {
        val a = Counter()
        val b = Counter()
        val wrapperA = Zeroizing(a)
        val wrapperB = Zeroizing(b)
        wrapperA.cloneFrom(wrapperB)
        assertEquals(1, a.zeroizeCalls)
    }

    @Test
    fun dropZeroizesInnerValue() {
        val counter = Counter()
        val wrapper = Zeroizing(counter)
        wrapper.drop()
        assertEquals(1, counter.zeroizeCalls)
    }

    @Test
    fun zeroizingSerializeAndDeserialize() {
        val secret = SerializableSecret("sensitive")
        val wrapper = Zeroizing(secret)
        val deserializer = Zeroizing.deserialize(SerializableSecretDeserialize)

        val serializer =
            object : Serializer<String> {
                override fun serializeBool(v: Boolean): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeI8(v: Byte): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeI16(v: Short): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeI32(v: Int): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeI64(v: Long): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeI128(value: String): SerdeResult<String> = SerdeResult.success(value)

                override fun serializeU8(v: UByte): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeU16(v: UShort): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeU32(v: UInt): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeU64(v: ULong): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeU128(value: String): SerdeResult<String> = SerdeResult.success(value)

                override fun serializeF32(v: Float): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeF64(v: Double): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeChar(v: Char): SerdeResult<String> = SerdeResult.success(v.toString())

                override fun serializeStr(v: String): SerdeResult<String> = SerdeResult.success(v)

                override fun serializeBytes(v: ByteArray): SerdeResult<String> = SerdeResult.success(v.decodeToString())

                override fun serializeNone(): SerdeResult<String> = SerdeResult.success("None")

                override fun <T : Serialize> serializeSome(value: T): SerdeResult<String> = value.serialize(this)

                override fun serializeUnit(): SerdeResult<String> = SerdeResult.success("()")

                override fun serializeUnitStruct(name: String): SerdeResult<String> = SerdeResult.success(name)

                override fun serializeUnitVariant(name: String, variantIndex: UInt, variant: String): SerdeResult<String> =
                    SerdeResult.success("$name::$variant")

                override fun <T : Serialize> serializeNewtypeStruct(name: String, value: T): SerdeResult<String> =
                    value.serialize(this)

                override fun <T : Serialize> serializeNewtypeVariant(name: String, variantIndex: UInt, variant: String, value: T): SerdeResult<String> =
                    value.serialize(this)

                override fun serializeSeq(len: Int?): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeSeq<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun serializeTuple(len: Int): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeTuple<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun serializeTupleStruct(name: String, len: Int): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeTupleStruct<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun serializeTupleVariant(name: String, variantIndex: UInt, variant: String, len: Int): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeTupleVariant<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun serializeMap(len: Int?): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeMap<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun serializeStruct(name: String, len: Int): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeStruct<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun serializeStructVariant(name: String, variantIndex: UInt, variant: String, len: Int): SerdeResult<io.github.kotlinmania.serdecore.ser.SerializeStructVariant<String>> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )
            }

        val serResult = wrapper.serialize(serializer)
        assertEquals("sensitive", serResult.getOrNull())

        val dummyDeserializer =
            object : Deserializer {
                override fun <V> deserializeAny(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeBool(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeI8(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeI16(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeI32(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeI64(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeU8(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeU16(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeU32(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeU64(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeF32(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeF64(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeChar(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeStr(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeString(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeBytes(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeByteBuf(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeOption(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeUnit(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeUnitStruct(name: String, visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeNewtypeStruct(name: String, visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeSeq(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeTuple(len: Int, visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeTupleStruct(name: String, len: Int, visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeMap(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeStruct(name: String, fields: List<String>, visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeEnum(name: String, variants: List<String>, visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeIdentifier(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )

                override fun <V> deserializeIgnoredAny(visitor: Visitor<V>): SerdeResult<V> =
                    SerdeResult.failure(
                        io.github.kotlinmania.serde.SerdeError
                            .custom("unsupported"),
                    )
            }

        val deResult = deserializer.deserialize(dummyDeserializer)
        assertEquals("deserialized-secret", deResult.getOrNull()?.value?.secret)
    }
}
