package me.ifmo;

import me.ifmo.function.MathFunction;
import me.ifmo.function.log.LnFunction;
import me.ifmo.function.log.Log10Function;
import me.ifmo.function.log.Log3Function;
import me.ifmo.function.system.SystemFunction;
import me.ifmo.function.trig.*;
import me.ifmo.util.CsvExporter;

public class Main {

    public static void main(String[] args) {
        double eps = 1e-6;

        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);

        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFunction(sin, cos);
        MathFunction csc = new CscFunction(sin);

        MathFunction ln = new LnFunction();
        MathFunction log3 = new Log3Function(ln);
        MathFunction log10 = new Log10Function(ln);

        MathFunction system = new SystemFunction(
                tan,
                csc,
                cot,
                ln,
                log3,
                log10
        );

        double[] xs = {-Math.PI / 4, -1.0, 0.5, 2.0};

        for (double x : xs) {
            System.out.println("f(" + x + ") = " + system.calculate(x, eps));
        }

        CsvExporter exporter = new CsvExporter();

        try {
            exporter.export(ln, 0.1, 5.0, 0.1, eps, "ln.csv");
            exporter.export(log3, 0.1, 5.0, 0.1, eps, "log3.csv");
            exporter.export(log10, 0.1, 5.0, 0.1, eps, "log10.csv");

            exporter.export(tan, -1.2, -0.2, 0.02, eps, "tan.csv");
            exporter.export(cot, -1.2, -0.2, 0.02, eps, "cot.csv");
            exporter.export(csc, -1.2, -0.2, 0.02, eps, "csc.csv");

            exporter.export(system, -1.2, 3.0, 0.02, eps, "system.csv");

            System.out.println("CSV files created successfully.");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}