package me.ifmo.stub;

import me.ifmo.function.MathFunction;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTableStub implements MathFunction {

    protected final Map<Double, Double> values = new HashMap<>();

    protected void put(double x, double y) {
        values.put(x, y);
    }

    @Override
    public double calculate(double x, double eps) {
        return values.getOrDefault(x, Double.NaN);
    }
}
