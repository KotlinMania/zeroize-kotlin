// port-lint: source lib.rs
package io.github.kotlinmania.zeroize

// The upstream crate provides a no-op Zeroize impl for PhantomData<Z>
// and a ZeroizeOnDrop impl, since PhantomData is zero-sized.
// Kotlin does not have phantom type markers — generics are reified at
// runtime. This semantic is not portable to Kotlin common code.
