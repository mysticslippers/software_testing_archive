package me.ifmo.function.log;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Log10FunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("log10(x): проверка деления ln(x) на ln(10) с подменённой зависимостью")
    void shouldCalculateLog10UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 10.0) {
                return 2.0;
            }
            return 8.0;
        };

        Log10Function log10Function = new Log10Function(lnStub);

        assertEquals(4.0, log10Function.calculate(5.0, EPS), DELTA);
    }

    @Test
    @DisplayName("log10(x): проверка вычисления дробного результата при подменённой зависимости")
    void shouldCalculateFractionalLog10UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 10.0) {
                return 4.0;
            }
            return 1.0;
        };

        Log10Function log10Function = new Log10Function(lnStub);

        assertEquals(0.25, log10Function.calculate(2.0, EPS), DELTA);
    }

    @Test
    @DisplayName("log10(10) = 1: проверка корректного деления одинаковых значений ln(10) / ln(10)")
    void shouldReturnOneForArgumentTen() {
        MathFunction lnStub = (x, eps) -> 9.0;

        Log10Function log10Function = new Log10Function(lnStub);

        assertEquals(1.0, log10Function.calculate(10.0, EPS), DELTA);
    }
}