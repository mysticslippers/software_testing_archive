package me.ifmo.function.log;

import me.ifmo.function.MathFunction;
import me.ifmo.util.CsvBackedMathFunction;
import me.ifmo.util.CsvExporter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Log3FunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = 0.25;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final String LN_CSV_FILE = "ln.csv";
    private static final String LOG3_CSV_FILE = "log3.csv";

    @BeforeAll
    static void ensureLnCsvExists() throws IOException {
        CsvExporter exporter = new CsvExporter();

        MathFunction realLn = new LnFunction();
        exporter.export(
                realLn,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                LN_CSV_FILE,
                "ln(X)"
        );
    }

    @Test
    @DisplayName("log3(x): проверка деления ln(x) на ln(3) с подменённой зависимостью")
    void shouldCalculateLog3UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 3.0) {
                return 2.0;
            }
            return 6.0;
        };

        Log3Function log3Function = new Log3Function(lnStub);

        assertEquals(3.0, log3Function.calculate(5.0, EPS), DELTA);
    }

    @Test
    @DisplayName("log3(x): проверка вычисления отрицательного результата при подменённой зависимости")
    void shouldCalculateNegativeLog3UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 3.0) {
                return 2.0;
            }
            return -4.0;
        };

        Log3Function log3Function = new Log3Function(lnStub);

        assertEquals(-2.0, log3Function.calculate(0.5, EPS), DELTA);
    }

    @Test
    @DisplayName("log3(3) = 1: проверка корректного деления одинаковых значений ln(3) / ln(3)")
    void shouldReturnOneForArgumentThree() {
        MathFunction lnStub = (x, eps) -> 7.0;

        Log3Function log3Function = new Log3Function(lnStub);

        assertEquals(1.0, log3Function.calculate(3.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция log3 -> ln.csv: значения должны совпадать с log3 на реальном LnFunction")
    void shouldCalculateLog3UsingCsvLn() throws IOException {
        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log3WithCsvDependency = new Log3Function(lnFromCsv);

        MathFunction realLn = new LnFunction();
        MathFunction log3WithRealDependency = new Log3Function(realLn);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double expected = log3WithRealDependency.calculate(x, EPS);
            double actual = log3WithCsvDependency.calculate(x, EPS);

            assertEquals(expected, actual, DELTA, "Mismatch at x = " + x);
        }
    }

    @Test
    @DisplayName("Интеграция log3 -> ln.csv: значения должны совпадать с эталонным log(x)/log(3)")
    void shouldMatchReferenceLog3UsingCsvLn() throws IOException {
        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log3WithCsvDependency = new Log3Function(lnFromCsv);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double actual = log3WithCsvDependency.calculate(x, EPS);
            double expected = Math.log(x) / Math.log(3.0);

            assertEquals(expected, actual, ETALON_DELTA, "Mismatch at x = " + x);
        }
    }

    @AfterAll
    static void exportLog3ValuesToCsv() throws IOException {
        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log3FromCsvDependency = new Log3Function(lnFromCsv);

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                log3FromCsvDependency,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                LOG3_CSV_FILE,
                "log3(X)"
        );
    }
}