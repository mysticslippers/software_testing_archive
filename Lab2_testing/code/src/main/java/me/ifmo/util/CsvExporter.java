package me.ifmo.util;

import me.ifmo.function.MathFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvExporter {

    public void export(
            MathFunction function,
            double start,
            double end,
            double step,
            double eps,
            String fileName
    ) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("X,Result");

            for (double x = start; x <= end; x += step) {
                double result = function.calculate(x, eps);
                writer.println(x + "," + result);
            }
        }
    }
}
