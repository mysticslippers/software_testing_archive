package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CscFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("csc(x): проверка вычисления 1 / sin(x) при положительном значении sin(x)")
    void shouldCalculateCscUsingStubFunction() {
        MathFunction sinStub = (x, eps) -> 2.0;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertEquals(0.5, cscFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("csc(x): проверка вычисления 1 / sin(x) при отрицательном значении sin(x)")
    void shouldCalculateNegativeCscUsingStubFunction() {
        MathFunction sinStub = (x, eps) -> -0.5;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertEquals(-2.0, cscFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("csc(x): должен возвращать NaN, если |sin(x)| меньше eps")
    void shouldReturnNaNWhenSinIsTooSmall() {
        MathFunction sinStub = (x, eps) -> 1e-7;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertTrue(Double.isNaN(cscFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("csc(x): должен возвращать NaN, если sin(x) равен нулю")
    void shouldReturnNaNWhenSinIsZero() {
        MathFunction sinStub = (x, eps) -> 0.0;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertTrue(Double.isNaN(cscFunction.calculate(1.0, EPS)));
    }
}