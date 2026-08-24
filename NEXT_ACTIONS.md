# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 3/3 (100.0%)
- **Function parity:** 8/17 matched (target 31) — 47.1%
- **Class/type parity:** 5/8 matched (target 5) — 62.5%
- **Combined symbol parity:** 13/25 matched (target 36) — 52.0%
- **Average inline-code cosine:** 0.77 (function body across 3 matched files)
- **Average documentation cosine:** 0.20 (doc text across 3 matched files)
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
- **Similarity:** 0.30
- **Dependents:** 0
- **Priority Score:** 122507.0
- **Functions:** 8/17 matched (target 31)
- **Missing functions:** `clone`, `clone_from`, `drop`, `serialize`, `deserialize`, `volatile_write`, `volatile_set`, `zeroize_flat_type`, `zeroize_or_on_drop`
- **Types:** 5/8 matched (target 5)
- **Missing types:** `Target`, `AssertZeroizeOnDrop`, `AssertZeroize`

### 2. x86

- **Target:** `zeroize.X86`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

### 3. aarch64

- **Target:** `zeroize.Aarch64`
- **Similarity:** 1.00
- **Dependents:** 0
- **Priority Score:** 0.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

