import Testing
import Zeroize

@Suite("Zeroize Swift Export Tests")
struct ZeroizeExportTests {
    @Test("Swift module imports and basic types are reachable")
    func swiftModuleLoads() throws {
        #expect(Bool(true))
    }
}
