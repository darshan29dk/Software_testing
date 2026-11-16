package com.qa.logic;

/** White-box target: checkout discount. 8 predicate nodes -> V(G) = 9. */
public class DiscountCalculator {
    public static double calculate(double subtotal, int items, boolean member) {
        if (subtotal <= 0) throw new IllegalArgumentException("Subtotal must be > 0");   // d1
        if (items <= 0) throw new IllegalArgumentException("Items must be > 0");         // d2
        int pct = 0;
        if (subtotal >= 1000) pct = 15;                                                  // d3
        else if (subtotal >= 500) pct = 10;                                              // d4
        else if (subtotal >= 100) pct = 5;                                               // d5
        if (items >= 10) pct += 2;                                                       // d6
        if (member) pct += 3;                                                            // d7
        if (pct > 18) pct = 18;                                                          // d8
        return Math.round(subtotal * (100 - pct)) / 100.0;
    }
}
