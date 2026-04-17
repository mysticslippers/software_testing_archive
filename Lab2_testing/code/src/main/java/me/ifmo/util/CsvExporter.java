package me.ifmo.util;

import me.ifmo.function.MathFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class CsvExporter {

    public void export(
            MathFunction function,
            double start,
            double end,
            double step,
            double eps,
            String fileName,
            String columnName
    ) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("X," + columnName);

            for (double x = start; x <= end; x += step) {
                try {
                    double result = function.calculate(x, eps);

                    if (Double.isNaN(result) || Double.isInfinite(result)) {
                        writer.printf(Locale.US, "%.6f,NaN%n", x);
                    } else {
                        writer.printf(Locale.US, "%.6f,%.10f%n", x, result);
                    }
                } catch (Exception e) {
                    writer.printf(Locale.US, "%.6f,ERROR%n", x);
                }
            }
        }
    }
}