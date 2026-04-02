package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;

public class CotFunction implements MathFunction {

    private final MathFunction sinFunction;
    private final MathFunction cosFunction;

    public CotFunction(MathFunction sinFunction, MathFunction cosFunction) {
        this.sinFunction = sinFunction;
        this.cosFunction = cosFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        double sin = sinFunction.calculate(x, eps);
        double cos = cosFunction.calculate(x, eps);

        if (Math.abs(sin) < eps) {
            return Double.NaN;
        }

        return cos / sin;
    }
}
