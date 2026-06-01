# zeroize-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fzeroize--kotlin-blue.svg)](https://github.com/KotlinMania/zeroize-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/zeroize-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/zeroize-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/zeroize-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/zeroize-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`RustCrypto/utils`](https://github.com/RustCrypto/utils).

**Original Project:** This port is based on [`RustCrypto/utils`](https://github.com/RustCrypto/utils). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `RustCrypto/utils`

> The text below is reproduced and lightly edited from [`https://github.com/RustCrypto/utils`](https://github.com/RustCrypto/utils). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## RustCrypto: Utilities

[![Project Chat][chat-image]][chat-link]
[![dependency status][deps-image]][deps-link]
![Apache2/MIT licensed][license-image]

This repository contains various utility crates used in the RustCrypto project.

## Crates

| Name | crates.io | Docs | Description |
|------|:---------:|:----:|-------------|
| [`aarch64-dit`] | [![crates.io](https://img.shields.io/crates/v/aarch64-dit.svg)](https://crates.io/crates/aarch64-dit) | [![Documentation](https://docs.rs/aarch64-dit/badge.svg)](https://docs.rs/aarch64-dit) | Wrappers for enabling/disabling the Data Independent Timing (DIT) feature on AArch64 CPUs |
| [`blobby`] | [![crates.io](https://img.shields.io/crates/v/blobby.svg)](https://crates.io/crates/blobby) | [![Documentation](https://docs.rs/blobby/badge.svg)](https://docs.rs/blobby) | Decoder of the simple de-duplicated binary blob storage format |
| [`block-buffer`] | [![crates.io](https://img.shields.io/crates/v/block-buffer.svg)](https://crates.io/crates/block-buffer) | [![Documentation](https://docs.rs/block-buffer/badge.svg)](https://docs.rs/block-buffer) | Fixed size buffer for block processing of data |
| [`block‑padding`] | [![crates.io](https://img.shields.io/crates/v/block-padding.svg)](https://crates.io/crates/block-padding) | [![Documentation](https://docs.rs/block-padding/badge.svg)](https://docs.rs/block-padding) | Padding and unpadding of messages divided into blocks |
| [`cmov`] | [![crates.io](https://img.shields.io/crates/v/cmov.svg)](https://crates.io/crates/cmov) | [![Documentation](https://docs.rs/cmov/badge.svg)](https://docs.rs/cmov) | Conditional move intrinsics |
| [`cpubits`] | [![crates.io](https://img.shields.io/crates/v/cpubits.svg)](https://crates.io/crates/cpubits) | [![Documentation](https://docs.rs/cpubits/badge.svg)](https://docs.rs/cpubits) | Compile-time detection heuristics for the optimal word size to use for the target CPU |
| [`cpufeatures`] | [![crates.io](https://img.shields.io/crates/v/cpufeatures.svg)](https://crates.io/crates/cpufeatures) | [![Documentation](https://docs.rs/cpufeatures/badge.svg)](https://docs.rs/cpufeatures) | Lightweight and efficient alternative to the `is_x86_feature_detected!` macro |
| [`ctutils`] | [![crates.io](https://img.shields.io/crates/v/ctutils.svg)](https://crates.io/crates/ctutils) | [![Documentation](https://docs.rs/ctutils/badge.svg)](https://docs.rs/ctutils) | Constant-time utility library with selection and equality testing support targeting cryptographic applications |
| [`dbl`] | [![crates.io](https://img.shields.io/crates/v/dbl.svg)](https://crates.io/crates/dbl) | [![Documentation](https://docs.rs/dbl/badge.svg)](https://docs.rs/dbl) | Double operation in Galois Field (GF) |
| [`digest-io`] | [![crates.io](https://img.shields.io/crates/v/digest-io.svg)](https://crates.io/crates/digest-io) | [![Documentation](https://docs.rs/digest-io/badge.svg)](https://docs.rs/digest-io) | `std::io`-compatibility wrappers for traits defined in the `digest` crate |
| [`hex-literal`] | [![crates.io](https://img.shields.io/crates/v/hex-literal.svg)](https://crates.io/crates/hex-literal) | [![Documentation](https://docs.rs/hex-literal/badge.svg)](https://docs.rs/hex-literal) | A macro for converting hexadecimal strings to a byte array at compile time |
| [`inout`] | [![crates.io](https://img.shields.io/crates/v/inout.svg)](https://crates.io/crates/inout) | [![Documentation](https://docs.rs/inout/badge.svg)](https://docs.rs/inout) | Custom reference types for code generic over in-place and buffer-to-buffer modes of operation. |
| [`wycheproof2blb`] |  |  | | Utility for converting [Wycheproof] test vectors to the blobby format |
| [`zeroize`] | [![crates.io](https://img.shields.io/crates/v/zeroize.svg)](https://crates.io/crates/zeroize) | [![Documentation](https://docs.rs/zeroize/badge.svg)](https://docs.rs/zeroize) | Securely zero memory while avoiding compiler optimizations |

## License

All crates licensed under either of

 * [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
 * [MIT license](http://opensource.org/licenses/MIT)

at your option.

### Contribution

Unless you explicitly state otherwise, any contribution intentionally submitted for inclusion in the work by you, as defined in the Apache-2.0 license, shall be dual licensed as above, without any additional terms or conditions.

[//]: # (badges)

[chat-image]: https://img.shields.io/badge/zulip-join_chat-blue.svg
[chat-link]: https://rustcrypto.zulipchat.com/#narrow/stream/260052-utils
[license-image]: https://img.shields.io/badge/license-Apache2.0/MIT-blue.svg
[deps-image]: https://deps.rs/repo/github/RustCrypto/utils/status.svg
[deps-link]: https://deps.rs/repo/github/RustCrypto/utils

[//]: # (crates)

[`aarch64-dit`]: ./aarch64-dit
[`blobby`]: ./blobby
[`block-buffer`]: ./block-buffer
[`block‑padding`]: ./block-padding
[`cmov`]: ./cmov
[`cpubits`]: ./cpubits
[`cpufeatures`]: ./cpufeatures
[`ctutils`]: ./ctutils
[`dbl`]: ./dbl
[`digest-io`]: ./digest-io
[`hex-literal`]: ./hex-literal
[`inout`]: ./inout
[`wycheproof2blb`]: ./wycheproof2blb
[`zeroize`]: ./zeroize

[//]: # (misc)

[Wycheproof]: https://github.com/google/wycheproof

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:zeroize-kotlin:0.1.1")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Wasm-WASI (Node.js)
- Android (API 24+)
- Android Native arm32 / arm64 / x86 / x64
- JVM

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same Apache-2.0 license as the upstream [`RustCrypto/utils`](https://github.com/RustCrypto/utils). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the utils authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`RustCrypto/utils`](https://github.com/RustCrypto/utils) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
