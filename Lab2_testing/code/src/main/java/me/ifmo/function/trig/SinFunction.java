package me.ifmo.function.trig;

public class SinFunction extends BaseTrigFunction {

    @Override
    public double calculate(double x, double eps) {
        validateEps(eps);
        x = normalizeAngle(x);

        double term = x;
        double sum = term;
        int n = 1;

        while (Math.abs(term) > eps) {
            term *= -1 * x * x / ((2.0 * n) * (2.0 * n + 1.0));
            sum += term;
            n++;

            if (n > 10000) {
                break;
            }
        }

        return sum;
    }
}
