package me.ifmo.util;

import me.ifmo.function.MathFunction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CsvBackedMathFunction implements MathFunction {

    private final Map<String, Double> values = new HashMap<>();

    public CsvBackedMathFunction(String fileName) throws IOException {
        load(fileName);
    }

    private void load(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }

                String xKey = normalize(parts[0].trim());
                String rawValue = parts[1].trim();

                double value;
                if ("NaN".equalsIgnoreCase(rawValue) || "ERROR".equalsIgnoreCase(rawValue)) {
                    value = Double.NaN;
                } else {
                    value = Double.parseDouble(rawValue);
                }

                values.put(xKey, value);
            }
        }
    }

    @Override
    public double calculate(double x, double epsilon) {
        String key = normalize(x);
        Double result = values.get(key);

        if (result == null) {
            throw new IllegalArgumentException("No CSV value for x = " + key);
        }

        return result;
    }

    private String normalize(double x) {
        return String.format(java.util.Locale.US, "%.6f", x);
    }

    private String normalize(String x) {
        return normalize(Double.parseDouble(x));
    }
}