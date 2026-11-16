# Cyclomatic Complexity Calculations

## 1. Theoretical Foundation
Cyclomatic Complexity, formulated by Thomas J. McCabe (1976), is a quantitative software metric used to indicate the logical complexity of a program. It directly measures the number of linearly independent paths through the program's source code and defines the minimum number of test cases required to achieve 100% branch and path coverage.

---

## 2. Parameter Extraction from Control Flow Graph

From the formal Control Flow Graph of `com.qa.logic.DiscountCalculator`:
- **Number of Nodes ($N$):** $17$
- **Number of Directed Edges ($E$):** $24$
- **Number of Connected Components ($P$):** $1$ (single coherent subroutine)
- **Number of Decision / Predicate Nodes ($D$):** $8$
  1. $d_1$: `subtotal <= 0` (Node 1)
  2. $d_2$: `items <= 0` (Node 3)
  3. $d_3$: `subtotal >= 1000` (Node 5)
  4. $d_4$: `subtotal >= 500` (Node 7)
  5. $d_5$: `subtotal >= 100` (Node 9)
  6. $d_6$: `items >= 10` (Node 11)
  7. $d_7$: `member == true` (Node 13)
  8. $d_8$: `pct > 18` (Node 15)

---

## 3. Step-by-Step Calculation using All Three Methods

### Method 1: Edge-Node Formula
$$V(G) = E - N + 2P$$
- Substitute $E = 24$, $N = 17$, $P = 1$:
$$V(G) = 24 - 17 + 2(1)$$
$$V(G) = 7 + 2 = 9$$

---

### Method 2: Predicate Node Formula
$$V(G) = D + 1$$
- Substitute $D = 8$:
$$V(G) = 8 + 1 = 9$$

---

### Method 3: Independent Regions in the Planar Graph
$$V(G) = \text{Total Number of Regions } (R)$$
In planar graph theory (Euler's formula for planar graphs), every enclosed cycle defines a bounded region, plus one unbounded exterior region encompassing the entire graph:
$$\text{Total Regions } R = R_{\text{enclosed}} + R_{\text{unbounded}}$$

#### Enumeration of Regions:
1. **Region $R_1$ (Bounded by $d_1$ Exception bypass):**
   Formed by path `(1 -> 2 -> 17)` and `(1 -> 3 -> 5 -> ... -> 17)`.
2. **Region $R_2$ (Bounded by $d_2$ Exception bypass):**
   Formed by path `(3 -> 4 -> 17)` and `(3 -> 5 -> ... -> 17)`.
3. **Region $R_3$ (Tier 1 vs Tier 2 subtotal branch):**
   Formed by `(5 -> 6 -> 11)` and `(5 -> 7 -> 8 -> 11)`.
4. **Region $R_4$ (Tier 2 vs Tier 3 subtotal branch):**
   Formed by `(7 -> 8 -> 11)` and `(7 -> 9 -> 10 -> 11)`.
5. **Region $R_5$ (Tier 3 vs No-Tier subtotal branch):**
   Formed by `(9 -> 10 -> 11)` and `(9 -> 11)`.
6. **Region $R_6$ (Volume discount branch):**
   Formed by `(11 -> 12 -> 13)` and `(11 -> 13)`.
7. **Region $R_7$ (Loyalty member discount branch):**
   Formed by `(13 -> 14 -> 15)` and `(13 -> 15)`.
8. **Region $R_8$ (Discount cap limit branch):**
   Formed by `(15 -> 16 -> 17)` and `(15 -> 17)`.
9. **Region $R_9$ (Unbounded Outer Region):**
   The entire external plane surrounding the graph.

$$\text{Total Regions } R = 8 \text{ (enclosed)} + 1 \text{ (outer)} = 9$$

---

## 4. Synthesis and Verification Summary

| Calculation Method | Mathematical Formula | Substitution | Computed $V(G)$ | Consistency Check |
|:---|:---|:---|:---:|:---:|
| **Method 1: Edge-Node** | $V(G) = E - N + 2P$ | $24 - 17 + 2(1) = 7 + 2$ | **9** | **MATCH** |
| **Method 2: Predicate Count** | $V(G) = D + 1$ | $8 + 1$ | **9** | **MATCH** |
| **Method 3: Planar Regions** | $V(G) = \text{Total Regions}$ | $8 \text{ enclosed} + 1 \text{ exterior}$ | **9** | **MATCH** |

All three independent mathematical formulations yield identical results ($V(G) = 9$), confirming the mathematical precision and completeness of the Control Flow Graph.
