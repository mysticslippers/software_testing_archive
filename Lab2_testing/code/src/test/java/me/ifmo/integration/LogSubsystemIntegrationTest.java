package me.ifmo.integration;

import me.ifmo.function.MathFunction;
import me.ifmo.function.log.LnFunction;
import me.ifmo.function.log.Log10Function;
import me.ifmo.function.log.Log3Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogSubsystemIntegrationTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;

    @Test
    @DisplayName("Интеграция снизу вверх: LnFunction + Log3Function")
    void shouldIntegrateLnWithLog3() {
        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);

        double x = 9.0;
        double expected = Math.log(x) / Math.log(3.0);

        assertEquals(expected, log3.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: LnFunction + Log10Function")
    void shouldIntegrateLnWithLog10() {
        MathFunction ln = new LnFunction();
        MathFunction log10 = new Log10Function(ln);

        double x = 100.0;
        double expected = Math.log10(x);

        assertEquals(expected, log10.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: LnFunction + Log3Function + Log10Function")
    void shouldIntegrateWholeLogSubsystem() {
        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);
        MathFunction log10 = new Log10Function(ln);

        double x = 3.0;

        assertEquals(Math.log(x), ln.calculate(x, EPS), DELTA);
        assertEquals(Math.log(x) / Math.log(3.0), log3.calculate(x, EPS), DELTA);
        assertEquals(Math.log10(x), log10.calculate(x, EPS), DELTA);
    }
}