package me.ifmo.function.log;

import me.ifmo.function.MathFunction;

public class LnFunction implements MathFunction {

    private static final int MAX_ITERATIONS = 100000;

    @Override
    public double calculate(double x, double eps) {
        validateInput(x, eps);

        double t = (x - 1) / (x + 1);
        double term = t;
        double sum = 0.0;

        int n = 0;
        while (Math.abs(term) > eps && n < MAX_ITERATIONS) {
            sum += term / (2 * n + 1);
            term *= t * t;
            n++;
        }

        return 2 * sum;
    }

    private void validateInput(double x, double eps) {
        if (x <= 0) {
            throw new IllegalArgumentException("ln(x) is undefined for x <= 0");
        }
        if (eps <= 0) {
            throw new IllegalArgumentException("Epsilon must be positive");
        }
    }
}
