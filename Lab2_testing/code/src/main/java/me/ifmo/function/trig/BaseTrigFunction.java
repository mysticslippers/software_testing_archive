package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;

public abstract class BaseTrigFunction implements MathFunction {

    protected void validateEps(double eps) {
        if (eps <= 0) {
            throw new IllegalArgumentException("Epsilon must be positive");
        }
    }

    protected double normalizeAngle(double x) {
        double twoPi = 2 * Math.PI;
        x = x % twoPi;

        if (x > Math.PI) {
            x -= twoPi;
        } else if (x < -Math.PI) {
            x += twoPi;
        }

        return x;
    }
}
