package me.ifmo.function.system;

import me.ifmo.function.MathFunction;

public class SystemFunction implements MathFunction {

    private final MathFunction tanFunction;
    private final MathFunction cscFunction;
    private final MathFunction cotFunction;

    private final MathFunction lnFunction;
    private final MathFunction log3Function;
    private final MathFunction log10Function;

    public SystemFunction(
            MathFunction tanFunction,
            MathFunction cscFunction,
            MathFunction cotFunction,
            MathFunction lnFunction,
            MathFunction log3Function,
            MathFunction log10Function
    ) {
        this.tanFunction = tanFunction;
        this.cscFunction = cscFunction;
        this.cotFunction = cotFunction;
        this.lnFunction = lnFunction;
        this.log3Function = log3Function;
        this.log10Function = log10Function;
    }

    @Override
    public double calculate(double x, double eps) {
        if (x <= 0) {
            return calculateTrigPart(x, eps);
        } else {
            return calculateLogPart(x, eps);
        }
    }

    private double calculateTrigPart(double x, double eps) {
        double tan = tanFunction.calculate(x, eps);
        double csc = cscFunction.calculate(x, eps);
        double cot = cotFunction.calculate(x, eps);

        if (Double.isNaN(tan) || Double.isNaN(csc) || Double.isNaN(cot)) {
            return Double.NaN;
        }

        double numeratorBase = (tan + csc + cot) / tan;
        double numerator = Math.pow(numeratorBase, 3) * cot;
        double denominator = Math.pow(cot, 3);

        if (Math.abs(denominator) < eps) {
            return Double.NaN;
        }

        return numerator / denominator;
    }

    private double calculateLogPart(double x, double eps) {
        double ln = lnFunction.calculate(x, eps);
        double log3 = log3Function.calculate(x, eps);
        double log10 = log10Function.calculate(x, eps);

        if (Double.isNaN(ln) || Double.isNaN(log3) || Double.isNaN(log10)) {
            return Double.NaN;
        }

        return ((((ln + log10) * log3) * (log3 - log3)) - log3) + log3;
    }
}
