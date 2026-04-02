package me.ifmo.function.log;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Log3FunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("log3(x): проверка деления ln(x) на ln(3) с подменённой зависимостью")
    void shouldCalculateLog3UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 3.0) {
                return 2.0;
            }
            return 6.0;
        };

        Log3Function log3Function = new Log3Function(lnStub);

        assertEquals(3.0, log3Function.calculate(5.0, EPS), DELTA);
    }

    @Test
    @DisplayName("log3(x): проверка вычисления отрицательного результата при подменённой зависимости")
    void shouldCalculateNegativeLog3UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 3.0) {
                return 2.0;
            }
            return -4.0;
        };

        Log3Function log3Function = new Log3Function(lnStub);

        assertEquals(-2.0, log3Function.calculate(0.5, EPS), DELTA);
    }

    @Test
    @DisplayName("log3(3) = 1: проверка корректного деления одинаковых значений ln(3) / ln(3)")
    void shouldReturnOneForArgumentThree() {
        MathFunction lnStub = (x, eps) -> 7.0;

        Log3Function log3Function = new Log3Function(lnStub);

        assertEquals(1.0, log3Function.calculate(3.0, EPS), DELTA);
    }
}