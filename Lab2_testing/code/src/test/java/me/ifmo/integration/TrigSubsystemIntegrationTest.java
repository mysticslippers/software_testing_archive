package me.ifmo.integration;

import me.ifmo.function.MathFunction;
import me.ifmo.function.trig.CosFunction;
import me.ifmo.function.trig.CotFunction;
import me.ifmo.function.trig.CscFunction;
import me.ifmo.function.trig.SinFunction;
import me.ifmo.function.trig.TanFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrigSubsystemIntegrationTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;

    @Test
    @DisplayName("Интеграция снизу вверх: SinFunction + CosFunction")
    void shouldIntegrateSinWithCos() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);

        double x = Math.PI / 3;

        assertEquals(Math.sin(x), sin.calculate(x, EPS), DELTA);
        assertEquals(Math.cos(x), cos.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: SinFunction + CosFunction + TanFunction")
    void shouldIntegrateSinCosWithTan() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);

        double x = Math.PI / 4;

        assertEquals(Math.tan(x), tan.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: SinFunction + CosFunction + CotFunction")
    void shouldIntegrateSinCosWithCot() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction cot = new CotFunction(sin, cos);

        double x = Math.PI / 4;
        double expected = Math.cos(x) / Math.sin(x);

        assertEquals(expected, cot.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: SinFunction + CscFunction")
    void shouldIntegrateSinWithCsc() {
        MathFunction sin = new SinFunction();
        MathFunction csc = new CscFunction(sin);

        double x = Math.PI / 6;
        double expected = 1.0 / Math.sin(x);

        assertEquals(expected, csc.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: вся тригонометрическая подсистема")
    void shouldIntegrateWholeTrigSubsystem() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction csc = new CscFunction(sin);

        double x = Math.PI / 6;

        assertEquals(Math.sin(x), sin.calculate(x, EPS), DELTA);
        assertEquals(Math.cos(x), cos.calculate(x, EPS), DELTA);
        assertEquals(Math.tan(x), tan.calculate(x, EPS), DELTA);
        assertEquals(Math.cos(x) / Math.sin(x), cot.calculate(x, EPS), DELTA);
        assertEquals(1.0 / Math.sin(x), csc.calculate(x, EPS), DELTA);
    }
}