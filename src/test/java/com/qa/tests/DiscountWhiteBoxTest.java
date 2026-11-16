package com.qa.tests;

import static org.assertj.core.api.Assertions.*;

import com.qa.logic.DiscountCalculator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DiscountWhiteBoxTest {

    @ParameterizedTest(name = "BVA subtotal={0} items={1} member={2} -> {3}")
    @DisplayName("BVA: tier boundaries (100 / 500 / 1000) and items boundary (10)")
    @CsvSource({
        "0.01,1,false,0.01",
        "99.99,1,false,99.99", "100.00,1,false,95.00", "100.01,1,false,95.01",
        "499.99,1,false,474.99", "500.00,1,false,450.00", "500.01,1,false,450.01",
        "999.99,1,false,899.99", "1000.00,1,false,850.00", "1000.01,1,false,850.01",
        "50.00,9,false,50.00", "50.00,10,false,49.00", "50.00,11,false,49.00"
    })
    void boundaries(double s, int n, boolean m, double expected) {
        assertThat(DiscountCalculator.calculate(s, n, m)).isEqualTo(expected, within(0.011));
    }

    @ParameterizedTest(name = "Basis path {0}")
    @DisplayName("White-box: 9 basis paths, V(G)=9")
    @CsvSource({
        "P3,1000,1,false,850.00",   // baseline: d3 T
        "P4,500,1,false,450.00",    // d3 F, d4 T
        "P5,100,1,false,95.00",     // d4 F, d5 T
        "P6,50,1,false,50.00",      // d5 F
        "P7,1000,10,false,830.00",  // d6 T
        "P8,1000,1,true,820.00",    // d7 T
        "P9,1000,10,true,820.00"    // d8 T (cap 18%)
    })
    void basisPaths(String id, double s, int n, boolean m, double expected) {
        assertThat(DiscountCalculator.calculate(s, n, m)).isEqualTo(expected, within(0.001));
    }

    @Test @DisplayName("Basis path P1: subtotal <= 0")
    void p1() { assertThatThrownBy(() -> DiscountCalculator.calculate(0, 1, false)).isInstanceOf(IllegalArgumentException.class); }

    @Test @DisplayName("Basis path P2: items <= 0")
    void p2() { assertThatThrownBy(() -> DiscountCalculator.calculate(10, 0, false)).isInstanceOf(IllegalArgumentException.class); }
}
