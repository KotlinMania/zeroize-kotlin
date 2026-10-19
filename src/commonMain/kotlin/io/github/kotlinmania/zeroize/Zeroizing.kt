@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.zeroize

import io.github.kotlinmania.serde.SerdeError
import io.github.kotlinmania.serde.SerdeResult
import io.github.kotlinmania.serdecore.de.Deserialize
import io.github.kotlinmania.serdecore.de.Deserializer
import io.github.kotlinmania.serdecore.ser.Serialize
import io.github.kotlinmania.serdecore.ser.Serializer
import kotlin.native.HiddenFromObjC

/**
 * [Zeroizing] is a wrapper for any `Z: Zeroize` type which calls [zeroize] on
 * the inner value when the wrapper is [closed][close].
 *
 * The wrapper itself also implements [Zeroize] so it can participate in
 * recursive zeroization.
 */
@HiddenFromObjC
class Zeroizing<Z : Zeroize>(
    /**
     * Move [value] inside a [Zeroizing] wrapper which ensures it will be
     * zeroized when it is closed.
     */
    var value: Z,
) : Zeroize,
    ZeroizeOnDrop,
    AutoCloseable,
    Serialize {
    @HiddenFromObjC
    public companion object {
        /**
         * Move [value] inside a [Zeroizing] wrapper which ensures it will be
         * zeroized when the wrapper is closed.
         */
        public fun <Z : Zeroize> new(value: Z): Zeroizing<Z> = Zeroizing(value)

        /**
         * Convert [value] into a [Zeroizing] wrapper.
         */
        public fun <Z : Zeroize> from(value: Z): Zeroizing<Z> = Zeroizing(value)

        /**
         * Construct a [Deserialize] implementation for [Zeroizing] of [inner].
         */
        public fun <Z : Zeroize> deserialize(inner: Deserialize<Z>): Deserialize<Zeroizing<Z>> =
            ZeroizingDeserialize(inner)
    }

    /**
     * Return the wrapped value by reference.
     */
    public fun deref(): Z = value

    /**
     * Return the wrapped value for mutation.
     */
    public fun derefMut(): Z = value

    /**
     * Return the wrapped value by reference.
     */
    public fun asRef(): Z = value

    /**
     * Return the wrapped value for mutation.
     */
    public fun asMut(): Z = value

    /**
     * Create a clone of this [Zeroizing] wrapper.
     */
    public fun clone(): Zeroizing<Z> = Zeroizing(value)

    /**
     * Replace this wrapper's value by zeroizing first and then adopting the source.
     */
    public fun cloneFrom(source: Zeroizing<Z>) {
        zeroize()
        value = source.value
    }

    /**
     * Drop and zeroize this wrapper.
     */
    public fun drop() {
        close()
    }

    override fun zeroize() {
        value.zeroize()
    }

    override fun close() {
        value.zeroize()
    }

    override fun <Ok> serialize(serializer: Serializer<Ok>): SerdeResult<Ok> {
        val v = value
        return if (v is Serialize) {
            v.serialize(serializer)
        } else {
            SerdeResult.failure(SerdeError.custom("inner value does not implement Serialize"))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Zeroizing<*>) return false
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "Zeroizing($value)"
}

/**
 * Serde [Deserialize] implementation for [Zeroizing] wrappers.
 */
@HiddenFromObjC
class ZeroizingDeserialize<Z : Zeroize>(
    private val inner: Deserialize<Z>,
) : Deserialize<Zeroizing<Z>> {
    override fun <D : Deserializer> deserialize(deserializer: D): SerdeResult<Zeroizing<Z>> =
        inner.deserialize(deserializer).map { Zeroizing(it) }

    override fun <D : Deserializer> deserializeInPlace(
        deserializer: D,
        place: (Zeroizing<Z>) -> Unit,
    ): SerdeResult<Unit> =
        inner.deserialize(deserializer).map { place(Zeroizing(it)) }
}

/**
 * Extension helper to create a [ZeroizingDeserialize] from an existing [Deserialize].
 */
@HiddenFromObjC
fun <Z : Zeroize> Deserialize<Z>.zeroizing(): Deserialize<Zeroizing<Z>> =
    ZeroizingDeserialize(this)
