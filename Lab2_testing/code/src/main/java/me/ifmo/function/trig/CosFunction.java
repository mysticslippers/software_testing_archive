package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;

public class CosFunction implements MathFunction {

    private final MathFunction sinFunction;

    public CosFunction(MathFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        return sinFunction.calculate(Math.PI / 2 - x, eps);
    }
}
