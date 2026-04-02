package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CotFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("cot(x): проверка деления cos(x) на sin(x) при положительных значениях")
    void shouldCalculateCotUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> 2.0;
        MathFunction cosStub = (x, eps) -> 6.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertEquals(3.0, cotFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("cot(x): проверка отрицательного результата при разных знаках cos(x) и sin(x)")
    void shouldCalculateNegativeCotUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> -2.0;
        MathFunction cosStub = (x, eps) -> 6.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertEquals(-3.0, cotFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("cot(x): должен возвращать NaN, если |sin(x)| меньше eps")
    void shouldReturnNaNWhenSinIsTooSmall() {
        MathFunction sinStub = (x, eps) -> 1e-7;
        MathFunction cosStub = (x, eps) -> 5.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(cotFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("cot(x): должен возвращать NaN, если sin(x) равен нулю")
    void shouldReturnNaNWhenSinIsZero() {
        MathFunction sinStub = (x, eps) -> 0.0;
        MathFunction cosStub = (x, eps) -> 5.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(cotFunction.calculate(1.0, EPS)));
    }
}