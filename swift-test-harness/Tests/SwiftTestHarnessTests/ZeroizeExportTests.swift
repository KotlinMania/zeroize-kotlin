import Testing
import Zeroize
import KotlinStdlib

@Suite("Zeroize Swift Export Tests")
struct ZeroizeExportTests {
    @Test("Zeroization clears a mutable string through the exported API")
    func zeroizeMutableString() {
        let buffer = kotlin.text.StringBuilder(content: "secret")
        #expect(buffer.length == 6)
        zeroize(buffer)
        #expect(buffer.length == 0)
        zeroize(buffer)
        #expect(buffer.length == 0)
    }

    @Test("Volatile write calls the Swift closure once and returns its object")
    func volatileWriteCallsOnce() {
        let buffer = kotlin.text.StringBuilder(content: "value")
        var calls = 0
        let result = volatileWrite {
            calls += 1
            return buffer
        }
        #expect(calls == 1)
        #expect((result as? kotlin.text.StringBuilder) === buffer)
    }
}
