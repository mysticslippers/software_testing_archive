package me.ifmo.function.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LnFunctionTest {

    private final LnFunction lnFunction = new LnFunction();
    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;

    @Test
    @DisplayName("ln(1) = 0: проверка базовой точки натурального логарифма")
    void shouldCalculateLnOfOne() {
        assertEquals(0.0, lnFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(2): проверка вычисления в стандартной положительной точке")
    void shouldCalculateLnOfTwo() {
        assertEquals(0.6931471805599453, lnFunction.calculate(2.0, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(3): проверка вычисления в ещё одной стандартной положительной точке")
    void shouldCalculateLnOfThree() {
        assertEquals(1.0986122886681098, lnFunction.calculate(3.0, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(10): проверка вычисления для большого стандартного аргумента")
    void shouldCalculateLnOfTen() {
        assertEquals(2.302585092994046, lnFunction.calculate(10.0, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(0.5): проверка вычисления для аргумента из интервала (0, 1)")
    void shouldCalculateLnOfOneHalf() {
        assertEquals(-0.6931471805599453, lnFunction.calculate(0.5, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(0.9): проверка значения слева от единицы")
    void shouldCalculateLnNearOneFromLeft() {
        double x = 0.9;
        assertEquals(Math.log(x), lnFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(1.1): проверка значения справа от единицы")
    void shouldCalculateLnNearOneFromRight() {
        double x = 1.1;
        assertEquals(Math.log(x), lnFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(5): проверка вычисления для произвольного положительного аргумента")
    void shouldCalculateLnForOrdinaryPositiveValue() {
        double x = 5.0;
        assertEquals(Math.log(x), lnFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("ln(0.2): проверка вычисления для малого положительного аргумента")
    void shouldCalculateLnForSmallPositiveValue() {
        double x = 0.2;
        assertEquals(Math.log(x), lnFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("x = 0: должен выбрасываться IllegalArgumentException, так как ln(x) не определён")
    void shouldThrowExceptionForZero() {
        assertThrows(IllegalArgumentException.class,
                () -> lnFunction.calculate(0.0, EPS));
    }

    @Test
    @DisplayName("x < 0: должен выбрасываться IllegalArgumentException, так как ln(x) не определён")
    void shouldThrowExceptionForNegativeX() {
        assertThrows(IllegalArgumentException.class,
                () -> lnFunction.calculate(-1.0, EPS));
    }

    @Test
    @DisplayName("eps = 0: должен выбрасываться IllegalArgumentException, так как точность должна быть положительной")
    void shouldThrowExceptionForZeroEps() {
        assertThrows(IllegalArgumentException.class,
                () -> lnFunction.calculate(2.0, 0.0));
    }

    @Test
    @DisplayName("eps < 0: должен выбрасываться IllegalArgumentException, так как точность должна быть положительной")
    void shouldThrowExceptionForNegativeEps() {
        assertThrows(IllegalArgumentException.class,
                () -> lnFunction.calculate(2.0, -1e-6));
    }
}