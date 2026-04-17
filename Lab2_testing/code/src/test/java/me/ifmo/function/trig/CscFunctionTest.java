package me.ifmo.function.trig;

import me.ifmo.function.MathFunction;
import me.ifmo.util.CsvExporter;
import me.ifmo.util.CsvMockitoUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CscFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = -100.0;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final double STABLE_SIN_THRESHOLD = 1e-2;

    private static final String SIN_CSV_FILE = "sin.csv";
    private static final String CSC_CSV_FILE = "csc.csv";

    @BeforeAll
    static void ensureCsvDependencyExists() throws IOException {
        CsvExporter exporter = new CsvExporter();

        MathFunction realSin = new SinFunction();
        exporter.export(
                realSin,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                SIN_CSV_FILE,
                "sin(X)"
        );
    }

    @Test
    @DisplayName("csc(x): проверка вычисления 1 / sin(x) при положительном значении sin(x)")
    void shouldCalculateCscUsingStubFunction() {
        MathFunction sinStub = (x, eps) -> 2.0;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertEquals(0.5, cscFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("csc(x): проверка вычисления 1 / sin(x) при отрицательном значении sin(x)")
    void shouldCalculateNegativeCscUsingStubFunction() {
        MathFunction sinStub = (x, eps) -> -0.5;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertEquals(-2.0, cscFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("csc(x): должен возвращать NaN, если |sin(x)| меньше eps")
    void shouldReturnNaNWhenSinIsTooSmall() {
        MathFunction sinStub = (x, eps) -> 1e-7;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertTrue(Double.isNaN(cscFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("csc(x): должен возвращать NaN, если sin(x) равен нулю")
    void shouldReturnNaNWhenSinIsZero() {
        MathFunction sinStub = (x, eps) -> 0.0;

        CscFunction cscFunction = new CscFunction(sinStub);

        assertTrue(Double.isNaN(cscFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Интеграция csc -> sin.csv: значения должны совпадать с csc на реальном Sin")
    void shouldCalculateCscUsingCsvSin() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cscWithCsvDependency = new CscFunction(sinFromCsv);

        MathFunction realSin = new SinFunction();
        MathFunction cscWithRealDependency = new CscFunction(realSin);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double expected = cscWithRealDependency.calculate(x, EPS);
            double actual = cscWithCsvDependency.calculate(x, EPS);

            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(actual), "Expected NaN at x = " + x);
            } else {
                assertEquals(expected, actual, DELTA, "Mismatch at x = " + x);
            }
        }
    }

    @Test
    @DisplayName("Интеграция csc -> sin.csv: значения должны совпадать с эталонным 1/sin(x) в устойчивых точках")
    void shouldMatchReferenceCscUsingCsvSin() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cscWithCsvDependency = new CscFunction(sinFromCsv);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            if (Math.abs(Math.sin(x)) < STABLE_SIN_THRESHOLD) {
                continue;
            }

            double actual = cscWithCsvDependency.calculate(x, EPS);

            if (!Double.isNaN(actual)) {
                double expected = 1.0 / Math.sin(x);
                assertEquals(expected, actual, ETALON_DELTA, "Mismatch at x = " + x);
            }
        }
    }

    @AfterAll
    static void exportCscValuesToCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cscFromCsvDependency = new CscFunction(sinFromCsv);

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                cscFromCsvDependency,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                CSC_CSV_FILE,
                "csc(X)"
        );
    }
}