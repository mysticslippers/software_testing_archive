package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;

public class CscFunction implements MathFunction {

    private final MathFunction sinFunction;

    public CscFunction(MathFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        double sin = sinFunction.calculate(x, eps);

        if (Math.abs(sin) < eps) {
            return Double.NaN;
        }

        return 1.0 / sin;
    }
}
