package me.ifmo.function.trig;

import me.ifmo.util.CsvExporter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SinFunctionTest {

    private static final SinFunction sinFunction = new SinFunction();

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;

    private static final double CSV_START = -100.0;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    @Test
    @DisplayName("sin(0) = 0: проверка базовой точки синуса")
    void shouldCalculateSinAtZero() {
        assertEquals(0.0, sinFunction.calculate(0.0, EPS), DELTA);
    }

    @Test
    @DisplayName("sin(pi/6) = 0.5: проверка стандартного положительного угла")
    void shouldCalculateSinAtPiDiv6() {
        assertEquals(0.5, sinFunction.calculate(Math.PI / 6, EPS), DELTA);
    }

    @Test
    @DisplayName("sin(pi/2) = 1: проверка точки максимума синуса")
    void shouldCalculateSinAtPiDiv2() {
        assertEquals(1.0, sinFunction.calculate(Math.PI / 2, EPS), DELTA);
    }

    @Test
    @DisplayName("sin(-pi/6) = -0.5: проверка стандартного отрицательного угла")
    void shouldCalculateSinAtMinusPiDiv6() {
        assertEquals(-0.5, sinFunction.calculate(-Math.PI / 6, EPS), DELTA);
    }

    @Test
    @DisplayName("sin(-pi/2) = -1: проверка точки минимума синуса")
    void shouldCalculateSinAtMinusPiDiv2() {
        assertEquals(-1.0, sinFunction.calculate(-Math.PI / 2, EPS), DELTA);
    }

    @Test
    @DisplayName("sin(pi) = 0: проверка нуля функции в точке pi")
    void shouldCalculateSinAtPi() {
        assertEquals(0.0, sinFunction.calculate(Math.PI, EPS), DELTA);
    }

    @Test
    @DisplayName("sin(-pi) = 0: проверка нуля функции в точке -pi")
    void shouldCalculateSinAtMinusPi() {
        assertEquals(0.0, sinFunction.calculate(-Math.PI, EPS), DELTA);
    }

    @Test
    @DisplayName("Нормализация угла больше 2pi: sin(2pi + pi/6) = sin(pi/6)")
    void shouldNormalizeAngleGreaterThanTwoPi() {
        double x = 2 * Math.PI + Math.PI / 6;
        assertEquals(0.5, sinFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Нормализация угла меньше -2pi: sin(-2pi - pi/6) = sin(-pi/6)")
    void shouldNormalizeAngleLessThanMinusTwoPi() {
        double x = -2 * Math.PI - Math.PI / 6;
        assertEquals(-0.5, sinFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Проверка sin(x) для произвольного положительного аргумента")
    void shouldCalculateSinForOrdinaryPositiveValue() {
        double x = 1.0;
        assertEquals(Math.sin(x), sinFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Проверка sin(x) для произвольного отрицательного аргумента")
    void shouldCalculateSinForOrdinaryNegativeValue() {
        double x = -1.0;
        assertEquals(Math.sin(x), sinFunction.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("eps = 0: должен выбрасываться IllegalArgumentException, так как точность должна быть положительной")
    void shouldThrowExceptionForZeroEps() {
        assertThrows(IllegalArgumentException.class,
                () -> sinFunction.calculate(1.0, 0.0));
    }

    @Test
    @DisplayName("eps < 0: должен выбрасываться IllegalArgumentException, так как точность должна быть положительной")
    void shouldThrowExceptionForNegativeEps() {
        assertThrows(IllegalArgumentException.class,
                () -> sinFunction.calculate(1.0, -1e-6));
    }

    @AfterAll
    static void exportSinValuesToCsv() throws IOException {
        CsvExporter exporter = new CsvExporter();
        exporter.export(
                sinFunction,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                "sin.csv",
                "sin(X)"
        );
    }
}