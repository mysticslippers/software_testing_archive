package me.ifmo.integration;

import me.ifmo.function.MathFunction;
import me.ifmo.function.log.LnFunction;
import me.ifmo.function.log.Log10Function;
import me.ifmo.function.log.Log3Function;
import me.ifmo.function.system.SystemFunction;
import me.ifmo.function.trig.CosFunction;
import me.ifmo.function.trig.CotFunction;
import me.ifmo.function.trig.CscFunction;
import me.ifmo.function.trig.SinFunction;
import me.ifmo.function.trig.TanFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SystemFunctionIntegrationTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;

    @Test
    @DisplayName("Интеграция снизу вверх: полная система в тригонометрической ветке при x <= 0")
    void shouldIntegrateWholeSystemForTrigBranch() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction csc = new CscFunction(sin);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);
        MathFunction log10 = new Log10Function(ln);

        MathFunction system = new SystemFunction(
                tan,
                csc,
                cot,
                ln,
                log3,
                log10
        );

        double x = -Math.PI / 4;

        double tanValue = Math.tan(x);
        double cotValue = Math.cos(x) / Math.sin(x);
        double cscValue = 1.0 / Math.sin(x);

        double expected = Math.pow((tanValue + cscValue + cotValue) / tanValue, 3)
                * cotValue / Math.pow(cotValue, 3);

        assertEquals(expected, system.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: полная система в логарифмической ветке при x > 0")
    void shouldIntegrateWholeSystemForLogBranch() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction csc = new CscFunction(sin);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);
        MathFunction log10 = new Log10Function(ln);

        MathFunction system = new SystemFunction(
                tan,
                csc,
                cot,
                ln,
                log3,
                log10
        );

        double x = 2.0;

        double expected = 0.0;

        assertEquals(expected, system.calculate(x, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция снизу вверх: полная система должна возвращать корректное числовое значение в допустимой тригонометрической точке")
    void shouldReturnFiniteValueForValidTrigPoint() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction csc = new CscFunction(sin);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);
        MathFunction log10 = new Log10Function(ln);

        MathFunction system = new SystemFunction(
                tan,
                csc,
                cot,
                ln,
                log3,
                log10
        );

        double result = system.calculate(-Math.PI / 6, EPS);

        assertFalse(Double.isNaN(result));
    }

    @Test
    @DisplayName("Интеграция снизу вверх: полная система должна возвращать корректное числовое значение в допустимой логарифмической точке")
    void shouldReturnFiniteValueForValidLogPoint() {
        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction csc = new CscFunction(sin);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);
        MathFunction log10 = new Log10Function(ln);

        MathFunction system = new SystemFunction(
                tan,
                csc,
                cot,
                ln,
                log3,
                log10
        );

        double result = system.calculate(3.0, EPS);

        assertFalse(Double.isNaN(result));
        assertEquals(0.0, result, DELTA);
    }
}