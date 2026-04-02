package me.ifmo.function.log;

import me.ifmo.function.MathFunction;

public class Log3Function implements MathFunction {

    private final MathFunction lnFunction;

    public Log3Function(MathFunction lnFunction) {
        this.lnFunction = lnFunction;
    }

    @Override
    public double calculate(double x, double eps) {
        return lnFunction.calculate(x, eps) / lnFunction.calculate(3.0, eps);
    }
}
