package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TanFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("tan(x): проверка деления sin(x) на cos(x) при положительных значениях")
    void shouldCalculateTanUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> 6.0;
        MathFunction cosStub = (x, eps) -> 2.0;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertEquals(3.0, tanFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("tan(x): проверка отрицательного результата при разных знаках sin(x) и cos(x)")
    void shouldCalculateNegativeTanUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> -6.0;
        MathFunction cosStub = (x, eps) -> 2.0;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertEquals(-3.0, tanFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("tan(x): должен возвращать NaN, если |cos(x)| меньше eps")
    void shouldReturnNaNWhenCosIsTooSmall() {
        MathFunction sinStub = (x, eps) -> 5.0;
        MathFunction cosStub = (x, eps) -> 1e-7;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(tanFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("tan(x): должен возвращать NaN, если cos(x) равен нулю")
    void shouldReturnNaNWhenCosIsZero() {
        MathFunction sinStub = (x, eps) -> 5.0;
        MathFunction cosStub = (x, eps) -> 0.0;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(tanFunction.calculate(1.0, EPS)));
    }
}