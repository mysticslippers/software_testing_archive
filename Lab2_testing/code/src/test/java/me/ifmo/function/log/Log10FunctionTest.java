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

class Log10FunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = 0.25;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final String LN_CSV_FILE = "ln.csv";
    private static final String LOG10_CSV_FILE = "log10.csv";

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
    @DisplayName("log10(x): проверка деления ln(x) на ln(10) с подменённой зависимостью")
    void shouldCalculateLog10UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 10.0) {
                return 2.0;
            }
            return 8.0;
        };

        Log10Function log10Function = new Log10Function(lnStub);

        assertEquals(4.0, log10Function.calculate(5.0, EPS), DELTA);
    }

    @Test
    @DisplayName("log10(x): проверка вычисления дробного результата при подменённой зависимости")
    void shouldCalculateFractionalLog10UsingStubLnFunction() {
        MathFunction lnStub = (x, eps) -> {
            if (x == 10.0) {
                return 4.0;
            }
            return 1.0;
        };

        Log10Function log10Function = new Log10Function(lnStub);

        assertEquals(0.25, log10Function.calculate(2.0, EPS), DELTA);
    }

    @Test
    @DisplayName("log10(10) = 1: проверка корректного деления одинаковых значений ln(10) / ln(10)")
    void shouldReturnOneForArgumentTen() {
        MathFunction lnStub = (x, eps) -> 9.0;

        Log10Function log10Function = new Log10Function(lnStub);

        assertEquals(1.0, log10Function.calculate(10.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Интеграция log10 -> ln.csv: значения должны совпадать с log10 на реальном LnFunction")
    void shouldCalculateLog10UsingCsvLn() throws IOException {
        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log10WithCsvDependency = new Log10Function(lnFromCsv);

        MathFunction realLn = new LnFunction();
        MathFunction log10WithRealDependency = new Log10Function(realLn);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double expected = log10WithRealDependency.calculate(x, EPS);
            double actual = log10WithCsvDependency.calculate(x, EPS);

            assertEquals(expected, actual, DELTA, "Mismatch at x = " + x);
        }
    }

    @Test
    @DisplayName("Интеграция log10 -> ln.csv: значения должны совпадать с эталонным Math.log10")
    void shouldMatchReferenceLog10UsingCsvLn() throws IOException {
        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log10WithCsvDependency = new Log10Function(lnFromCsv);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double actual = log10WithCsvDependency.calculate(x, EPS);
            double expected = Math.log10(x);

            assertEquals(expected, actual, ETALON_DELTA, "Mismatch at x = " + x);
        }
    }

    @AfterAll
    static void exportLog10ValuesToCsv() throws IOException {
        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log10FromCsvDependency = new Log10Function(lnFromCsv);

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                log10FromCsvDependency,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                LOG10_CSV_FILE,
                "log10(X)"
        );
    }
}