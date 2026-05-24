# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/3 (33.3%)
- **Function parity:** 8/17 matched (target 26) — 47.1%
- **Class/type parity:** 5/8 matched (target 5) — 62.5%
- **Combined symbol parity:** 13/25 matched (target 31) — 52.0%
- **Average inline-code cosine:** 0.27 (function body across 1 matched files)
- **Average documentation cosine:** 0.60 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `zeroize.Zeroize`
- **Similarity:** 0.27
- **Dependents:** 0
- **Priority Score:** 122507.3
- **Functions:** 8/17 matched (target 26)
- **Missing functions:** `clone`, `clone_from`, `drop`, `serialize`, `deserialize`, `volatile_write`, `volatile_set`, `zeroize_flat_type`, `zeroize_or_on_drop`
- **Types:** 5/8 matched (target 5)
- **Missing types:** `Target`, `AssertZeroizeOnDrop`, `AssertZeroize`

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

