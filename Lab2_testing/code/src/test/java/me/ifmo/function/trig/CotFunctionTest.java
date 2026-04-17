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

class CotFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = -100.0;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final double STABLE_SIN_THRESHOLD = 1e-2;

    private static final String SIN_CSV_FILE = "sin.csv";
    private static final String COS_CSV_FILE = "cos.csv";
    private static final String COT_CSV_FILE = "cot.csv";

    @BeforeAll
    static void ensureCsvDependenciesExist() throws IOException {
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

        MathFunction realCos = new CosFunction(realSin);
        exporter.export(
                realCos,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                COS_CSV_FILE,
                "cos(X)"
        );
    }

    @Test
    @DisplayName("cot(x): проверка деления cos(x) на sin(x) при положительных значениях")
    void shouldCalculateCotUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> 2.0;
        MathFunction cosStub = (x, eps) -> 6.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertEquals(3.0, cotFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("cot(x): проверка отрицательного результата при разных знаках cos(x) и sin(x)")
    void shouldCalculateNegativeCotUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> -2.0;
        MathFunction cosStub = (x, eps) -> 6.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertEquals(-3.0, cotFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("cot(x): должен возвращать NaN, если |sin(x)| меньше eps")
    void shouldReturnNaNWhenSinIsTooSmall() {
        MathFunction sinStub = (x, eps) -> 1e-7;
        MathFunction cosStub = (x, eps) -> 5.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(cotFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("cot(x): должен возвращать NaN, если sin(x) равен нулю")
    void shouldReturnNaNWhenSinIsZero() {
        MathFunction sinStub = (x, eps) -> 0.0;
        MathFunction cosStub = (x, eps) -> 5.0;

        CotFunction cotFunction = new CotFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(cotFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Интеграция cot -> sin.csv + cos.csv: значения должны совпадать с cot на реальных Sin/Cos")
    void shouldCalculateCotUsingCsvSinAndCsvCos() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);

        MathFunction cotWithCsvDependencies = new CotFunction(sinFromCsv, cosFromCsv);

        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction cotWithRealDependencies = new CotFunction(realSin, realCos);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double expected = cotWithRealDependencies.calculate(x, EPS);
            double actual = cotWithCsvDependencies.calculate(x, EPS);

            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(actual), "Expected NaN at x = " + x);
            } else {
                assertEquals(expected, actual, DELTA, "Mismatch at x = " + x);
            }
        }
    }

    @Test
    @DisplayName("Интеграция cot -> sin.csv + cos.csv: значения должны совпадать с эталонным cos(x)/sin(x) в устойчивых точках")
    void shouldMatchReferenceCotUsingCsvSinAndCsvCos() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);

        MathFunction cotWithCsvDependencies = new CotFunction(sinFromCsv, cosFromCsv);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            if (Math.abs(Math.sin(x)) < STABLE_SIN_THRESHOLD) {
                continue;
            }

            double actual = cotWithCsvDependencies.calculate(x, EPS);

            if (!Double.isNaN(actual)) {
                double expected = Math.cos(x) / Math.sin(x);
                assertEquals(expected, actual, ETALON_DELTA, "Mismatch at x = " + x);
            }
        }
    }

    @AfterAll
    static void exportCotValuesToCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);
        MathFunction cotFromCsvDependencies = new CotFunction(sinFromCsv, cosFromCsv);

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                cotFromCsvDependencies,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                COT_CSV_FILE,
                "cot(X)"
        );
    }
}