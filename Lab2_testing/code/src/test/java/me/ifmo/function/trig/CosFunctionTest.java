package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CosFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

    @Test
    @DisplayName("cos(x): должен возвращать значение, полученное от зависимости sin(pi/2 - x)")
    void shouldReturnValueFromSinDependency() {
        MathFunction sinStub = (x, eps) -> 42.0;

        CosFunction cosFunction = new CosFunction(sinStub);

        assertEquals(42.0, cosFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("cos(x): должен передавать в sinFunction аргумент вида pi/2 - x")
    void shouldPassShiftedArgumentToSinFunction() {
        final double originalX = 0.3;

        MathFunction sinStub = (x, eps) -> x;

        CosFunction cosFunction = new CosFunction(sinStub);

        assertEquals(Math.PI / 2 - originalX,
                cosFunction.calculate(originalX, EPS),
                DELTA);
    }

    @Test
    @DisplayName("cos(x): должен корректно передавать отрицательный аргумент в преобразованном виде")
    void shouldPassShiftedNegativeArgumentToSinFunction() {
        final double originalX = -1.2;

        MathFunction sinStub = (x, eps) -> x;

        CosFunction cosFunction = new CosFunction(sinStub);

        assertEquals(Math.PI / 2 - originalX,
                cosFunction.calculate(originalX, EPS),
                DELTA);
    }
}