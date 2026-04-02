package me.ifmo.function.system;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("x <= 0: должна использоваться тригонометрическая ветка системы")
    void shouldUseTrigBranchForNonPositiveX() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> 1.0;
        MathFunction cotStub = (x, eps) -> 4.0;

        MathFunction lnStub = (x, eps) -> 100.0;
        MathFunction log3Stub = (x, eps) -> 100.0;
        MathFunction log10Stub = (x, eps) -> 100.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = Math.pow((2.0 + 1.0 + 4.0) / 2.0, 3) * 4.0 / Math.pow(4.0, 3);

        assertEquals(expected, systemFunction.calculate(0.0, EPS), DELTA);
    }

    @Test
    @DisplayName("x > 0: должна использоваться логарифмическая ветка системы")
    void shouldUseLogBranchForPositiveX() {
        MathFunction tanStub = (x, eps) -> 999.0;
        MathFunction cscStub = (x, eps) -> 999.0;
        MathFunction cotStub = (x, eps) -> 999.0;

        MathFunction lnStub = (x, eps) -> 2.0;
        MathFunction log3Stub = (x, eps) -> 5.0;
        MathFunction log10Stub = (x, eps) -> 3.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = ((((2.0 + 3.0) * 5.0) * (5.0 - 5.0)) - 5.0) + 5.0;

        assertEquals(expected, systemFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Тригонометрическая ветка: корректное вычисление по формуле на подставных значениях")
    void shouldCalculateTrigBranchUsingStubValues() {
        MathFunction tanStub = (x, eps) -> 1.0;
        MathFunction cscStub = (x, eps) -> 2.0;
        MathFunction cotStub = (x, eps) -> 2.0;

        MathFunction lnStub = (x, eps) -> 0.0;
        MathFunction log3Stub = (x, eps) -> 0.0;
        MathFunction log10Stub = (x, eps) -> 0.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = Math.pow((1.0 + 2.0 + 2.0) / 1.0, 3) * 2.0 / Math.pow(2.0, 3);

        assertEquals(expected, systemFunction.calculate(-1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Логарифмическая ветка: корректное вычисление по формуле на подставных значениях")
    void shouldCalculateLogBranchUsingStubValues() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> 7.0;
        MathFunction log3Stub = (x, eps) -> 4.0;
        MathFunction log10Stub = (x, eps) -> 1.5;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = ((((7.0 + 1.5) * 4.0) * (4.0 - 4.0)) - 4.0) + 4.0;

        assertEquals(expected, systemFunction.calculate(2.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если tan(x) вернул NaN")
    void shouldReturnNaNWhenTanIsNaN() {
        MathFunction tanStub = (x, eps) -> Double.NaN;
        MathFunction cscStub = (x, eps) -> 2.0;
        MathFunction cotStub = (x, eps) -> 3.0;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если csc(x) вернул NaN")
    void shouldReturnNaNWhenCscIsNaN() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> Double.NaN;
        MathFunction cotStub = (x, eps) -> 3.0;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если cot(x) вернул NaN")
    void shouldReturnNaNWhenCotIsNaN() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> 3.0;
        MathFunction cotStub = (x, eps) -> Double.NaN;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если знаменатель |cot(x)^3| меньше eps")
    void shouldReturnNaNWhenTrigDenominatorIsTooSmall() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> 3.0;
        MathFunction cotStub = (x, eps) -> 0.001;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Логарифмическая ветка: должна возвращать NaN, если ln(x) вернул NaN")
    void shouldReturnNaNWhenLnIsNaN() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> Double.NaN;
        MathFunction log3Stub = (x, eps) -> 2.0;
        MathFunction log10Stub = (x, eps) -> 3.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Логарифмическая ветка: должна возвращать NaN, если log3(x) вернул NaN")
    void shouldReturnNaNWhenLog3IsNaN() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> 2.0;
        MathFunction log3Stub = (x, eps) -> Double.NaN;
        MathFunction log10Stub = (x, eps) -> 3.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Логарифмическая ветка: должна возвращать NaN, если log10(x) вернул NaN")
    void shouldReturnNaNWhenLog10IsNaN() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> 2.0;
        MathFunction log3Stub = (x, eps) -> 4.0;
        MathFunction log10Stub = (x, eps) -> Double.NaN;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(1.0, EPS)));
    }
}