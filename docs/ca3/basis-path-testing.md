# Basis Path Testing (White-Box Analysis)

## 1. Overview
Basis Path Testing is a white-box testing technique based on the control structure of a program. A **basis set** is a set of linearly independent paths through a program's Control Flow Graph such that any arbitrary path through the program can be expressed as a linear combination of paths from this set.

The Cyclomatic Complexity metric ($V(G) = 9$) establishes that exactly **9 independent basis paths** are required to form a complete basis set guaranteeing 100% statement and branch coverage.

---

## 2. Derivation of Independent Basis Paths

A path is linearly independent if it introduces at least one edge that has not been traversed by any previously defined path in the basis set.

### Path 1 ($P_1$): Subtotal Negative or Zero Exception
- **Node Sequence:** `1 -> 2 -> 17`
- **New Edge Introduced:** `(1 -> 2)`, `(2 -> 17)`
- **Conditions:** `subtotal <= 0` ($d_1 = \text{True}$)
- **Test Input:** `subtotal = 0.00`, `items = 1`, `member = false`
- **Expected Result:** Throws `IllegalArgumentException("Subtotal must be > 0")`
- **JUnit Mapping:** `DiscountWhiteBoxTest.p1()`

### Path 2 ($P_2$): Items Zero or Negative Exception
- **Node Sequence:** `1 -> 3 -> 4 -> 17`
- **New Edge Introduced:** `(1 -> 3)`, `(3 -> 4)`, `(4 -> 17)`
- **Conditions:** `subtotal > 0` ($d_1 = \text{False}$), `items <= 0` ($d_2 = \text{True}$)
- **Test Input:** `subtotal = 10.00`, `items = 0`, `member = false`
- **Expected Result:** Throws `IllegalArgumentException("Items must be > 0")`
- **JUnit Mapping:** `DiscountWhiteBoxTest.p2()`

### Path 3 ($P_3$): Baseline Mainline Path (Tier 1 Discount)
- **Node Sequence:** `1 -> 3 -> 5 -> 6 -> 11 -> 13 -> 15 -> 17`
- **New Edge Introduced:** `(3 -> 5)`, `(5 -> 6)`, `(6 -> 11)`, `(11 -> 13)`, `(13 -> 15)`, `(15 -> 17)`
- **Conditions:** $d_1 = \text{False}$, $d_2 = \text{False}$, $d_3 = \text{True}$ (`subtotal >= 1000`), $d_6 = \text{False}$ (`items < 10`), $d_7 = \text{False}$ (`member = false`), $d_8 = \text{False}$ (`pct <= 18`)
- **Calculation:** `pct = 15`. Discounted total = $1000 \times (100 - 15) / 100 = 850.00$
- **Test Input:** `subtotal = 1000.00`, `items = 1`, `member = false`
- **Expected Result:** `850.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P3", 1000, 1, false, 850.00)`

### Path 4 ($P_4$): Tier 2 Discount Path
- **Node Sequence:** `1 -> 3 -> 5 -> 7 -> 8 -> 11 -> 13 -> 15 -> 17`
- **New Edge Introduced:** `(5 -> 7)`, `(7 -> 8)`, `(8 -> 11)`
- **Conditions:** $d_3 = \text{False}$, $d_4 = \text{True}$ (`500 <= subtotal < 1000`), all other conditions same as baseline
- **Calculation:** `pct = 10`. Discounted total = $500 \times (100 - 10) / 100 = 450.00$
- **Test Input:** `subtotal = 500.00`, `items = 1`, `member = false`
- **Expected Result:** `450.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P4", 500, 1, false, 450.00)`

### Path 5 ($P_5$): Tier 3 Discount Path
- **Node Sequence:** `1 -> 3 -> 5 -> 7 -> 9 -> 10 -> 11 -> 13 -> 15 -> 17`
- **New Edge Introduced:** `(7 -> 9)`, `(9 -> 10)`, `(10 -> 11)`
- **Conditions:** $d_3 = \text{False}$, $d_4 = \text{False}$, $d_5 = \text{True}$ (`100 <= subtotal < 500`), all other conditions same as baseline
- **Calculation:** `pct = 5`. Discounted total = $100 \times (100 - 5) / 100 = 95.00$
- **Test Input:** `subtotal = 100.00`, `items = 1`, `member = false`
- **Expected Result:** `95.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P5", 100, 1, false, 95.00)`

### Path 6 ($P_6$): No Tier Base Discount Path
- **Node Sequence:** `1 -> 3 -> 5 -> 7 -> 9 -> 11 -> 13 -> 15 -> 17`
- **New Edge Introduced:** `(9 -> 11)`
- **Conditions:** $d_3 = \text{False}$, $d_4 = \text{False}$, $d_5 = \text{False}$ (`subtotal < 100`), all other conditions same as baseline
- **Calculation:** `pct = 0`. Discounted total = $50 \times (100 - 0) / 100 = 50.00$
- **Test Input:** `subtotal = 50.00`, `items = 1`, `member = false`
- **Expected Result:** `50.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P6", 50, 1, false, 50.00)`

### Path 7 ($P_7$): Volume Quantity Discount Path
- **Node Sequence:** `1 -> 3 -> 5 -> 6 -> 11 -> 12 -> 13 -> 15 -> 17`
- **New Edge Introduced:** `(11 -> 12)`, `(12 -> 13)`
- **Conditions:** Baseline with $d_6 = \text{True}$ (`items >= 10`)
- **Calculation:** `pct = 15 + 2 = 17`. Discounted total = $1000 \times (100 - 17) / 100 = 830.00$
- **Test Input:** `subtotal = 1000.00`, `items = 10`, `member = false`
- **Expected Result:** `830.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P7", 1000, 10, false, 830.00)`

### Path 8 ($P_8$): Membership Loyalty Discount Path
- **Node Sequence:** `1 -> 3 -> 5 -> 6 -> 11 -> 13 -> 14 -> 15 -> 17`
- **New Edge Introduced:** `(13 -> 14)`, `(14 -> 15)`
- **Conditions:** Baseline with $d_7 = \text{True}$ (`member = true`)
- **Calculation:** `pct = 15 + 3 = 18`. Discounted total = $1000 \times (100 - 18) / 100 = 820.00$
- **Test Input:** `subtotal = 1000.00`, `items = 1`, `member = true`
- **Expected Result:** `820.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P8", 1000, 1, true, 820.00)`

### Path 9 ($P_9$): Maximum Discount Ceiling Cap Path
- **Node Sequence:** `1 -> 3 -> 5 -> 6 -> 11 -> 12 -> 13 -> 14 -> 15 -> 16 -> 17`
- **New Edge Introduced:** `(15 -> 16)`, `(16 -> 17)`
- **Conditions:** Baseline with $d_6 = \text{True}$ (`items >= 10`) AND $d_7 = \text{True}$ (`member = true`). Initial `pct = 15 + 2 + 3 = 20`. Since $20 > 18$, $d_8 = \text{True}$ triggers `pct = 18`.
- **Calculation:** `pct = 18` (capped). Discounted total = $1000 \times (100 - 18) / 100 = 820.00$
- **Test Input:** `subtotal = 1000.00`, `items = 10`, `member = true`
- **Expected Result:** `820.00`
- **JUnit Mapping:** `DiscountWhiteBoxTest.basisPaths("P9", 1000, 10, true, 820.00)`

---

## 3. Basis Path Traceability Matrix

| Basis Path | Traversed Node Sequence | Conditions / Branch Values | Test Method | Input Vector `(s, n, m)` | Expected Output | Status |
|:---:|:---|:---|:---|:---:|:---:|:---:|
| **$P_1$** | `1-2-17` | $d_1: T$ | `p1()` | `(0, 1, false)` | `IllegalArgumentException` | **PASS** |
| **$P_2$** | `1-3-4-17` | $d_1: F, d_2: T$ | `p2()` | `(10, 0, false)` | `IllegalArgumentException` | **PASS** |
| **$P_3$** | `1-3-5-6-11-13-15-17` | $d_1: F, d_2: F, d_3: T, d_6: F, d_7: F, d_8: F$ | `basisPaths(...)` | `(1000, 1, false)` | `850.00` | **PASS** |
| **$P_4$** | `1-3-5-7-8-11-13-15-17` | $d_3: F, d_4: T$ | `basisPaths(...)` | `(500, 1, false)` | `450.00` | **PASS** |
| **$P_5$** | `1-3-5-7-9-10-11-13-15-17` | $d_3: F, d_4: F, d_5: T$ | `basisPaths(...)` | `(100, 1, false)` | `95.00` | **PASS** |
| **$P_6$** | `1-3-5-7-9-11-13-15-17` | $d_3: F, d_4: F, d_5: F$ | `basisPaths(...)` | `(50, 1, false)` | `50.00` | **PASS** |
| **$P_7$** | `1-3-5-6-11-12-13-15-17` | $d_3: T, d_6: T$ | `basisPaths(...)` | `(1000, 10, false)` | `830.00` | **PASS** |
| **$P_8$** | `1-3-5-6-11-13-14-15-17` | $d_3: T, d_7: T$ | `basisPaths(...)` | `(1000, 1, true)` | `820.00` | **PASS** |
| **$P_9$** | `1-3-5-6-11-12-13-14-15-16-17` | $d_3: T, d_6: T, d_7: T, d_8: T$ | `basisPaths(...)` | `(1000, 10, true)` | `820.00` | **PASS** |

All 9 independent basis paths are covered with 100% decision and statement coverage in [`DiscountWhiteBoxTest.java`](file:///c:/Users/Darshan/Downloads/selenium-qa-project/src/test/java/com/qa/tests/DiscountWhiteBoxTest.java).
