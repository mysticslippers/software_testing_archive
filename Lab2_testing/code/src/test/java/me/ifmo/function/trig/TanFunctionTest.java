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

class TanFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = -100.0;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final double STABLE_COS_THRESHOLD = 1e-2;

    private static final String SIN_CSV_FILE = "sin.csv";
    private static final String COS_CSV_FILE = "cos.csv";
    private static final String TAN_CSV_FILE = "tan.csv";

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
    @DisplayName("tan(x): проверка деления sin(x) на cos(x) при положительных значениях")
    void shouldCalculateTanUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> 6.0;
        MathFunction cosStub = (x, eps) -> 2.0;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertEquals(3.0, tanFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("tan(x): проверка отрицательного результата при разных знаках sin(x) и cos(x)")
    void shouldCalculateNegativeTanUsingStubFunctions() {
        MathFunction sinStub = (x, eps) -> -6.0;
        MathFunction cosStub = (x, eps) -> 2.0;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertEquals(-3.0, tanFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("tan(x): должен возвращать NaN, если |cos(x)| меньше eps")
    void shouldReturnNaNWhenCosIsTooSmall() {
        MathFunction sinStub = (x, eps) -> 5.0;
        MathFunction cosStub = (x, eps) -> 1e-7;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(tanFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("tan(x): должен возвращать NaN, если cos(x) равен нулю")
    void shouldReturnNaNWhenCosIsZero() {
        MathFunction sinStub = (x, eps) -> 5.0;
        MathFunction cosStub = (x, eps) -> 0.0;

        TanFunction tanFunction = new TanFunction(sinStub, cosStub);

        assertTrue(Double.isNaN(tanFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Интеграция tan -> sin.csv + cos.csv: значения должны совпадать с tan на реальных Sin/Cos")
    void shouldCalculateTanUsingCsvSinAndCsvCos() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);

        MathFunction tanWithCsvDependencies = new TanFunction(sinFromCsv, cosFromCsv);

        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction tanWithRealDependencies = new TanFunction(realSin, realCos);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            double expected = tanWithRealDependencies.calculate(x, EPS);
            double actual = tanWithCsvDependencies.calculate(x, EPS);

            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(actual), "Expected NaN at x = " + x);
            } else {
                assertEquals(expected, actual, DELTA, "Mismatch at x = " + x);
            }
        }
    }

    @Test
    @DisplayName("Интеграция tan -> sin.csv + cos.csv: значения должны совпадать с эталонным Math.tan в устойчивых точках")
    void shouldMatchMathTanUsingCsvSinAndCsvCos() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);

        MathFunction tanWithCsvDependencies = new TanFunction(sinFromCsv, cosFromCsv);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            if (Math.abs(Math.cos(x)) < STABLE_COS_THRESHOLD) {
                continue;
            }

            double actual = tanWithCsvDependencies.calculate(x, EPS);

            if (!Double.isNaN(actual)) {
                assertEquals(Math.tan(x), actual, ETALON_DELTA, "Mismatch at x = " + x);
            }
        }
    }

    @AfterAll
    static void exportTanValuesToCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);
        MathFunction tanFromCsvDependencies = new TanFunction(sinFromCsv, cosFromCsv);

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                tanFromCsvDependencies,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                TAN_CSV_FILE,
                "tan(X)"
        );
    }
}