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

class CosFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;

    private static final double SIN_CSV_START = -100.0;
    private static final double SIN_CSV_END = 100.0;
    private static final double SIN_CSV_STEP = 0.25;

    private static final double COS_CSV_START = Math.PI / 2 - SIN_CSV_END;
    private static final double COS_CSV_END = Math.PI / 2 - SIN_CSV_START;
    private static final double COS_CSV_STEP = SIN_CSV_STEP;

    private static final String SIN_CSV_FILE = "sin.csv";
    private static final String COS_CSV_FILE = "cos.csv";

    @BeforeAll
    static void ensureSinCsvExists() throws IOException {
        CsvExporter exporter = new CsvExporter();
        exporter.export(
                new SinFunction(),
                SIN_CSV_START,
                SIN_CSV_END,
                SIN_CSV_STEP,
                EPS,
                SIN_CSV_FILE,
                "sin(X)"
        );
    }

    @Test
    @DisplayName("cos(x): должен возвращать значение, полученное от зависимости sin(pi/2 - x)")
    void shouldReturnValueFromSinDependency() {
        MathFunction sinStub = (x, eps) -> 42.0;

        CosFunction cosFunction = new CosFunction(sinStub);

        assertEquals(42.0, cosFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("cos(x): должен передавать в sinFunction аргумент вида pi/2 - x")
    void shouldPassShiftedArgumentToSinFunction() {
        final double originalX = 0.3;

        MathFunction sinStub = (x, eps) -> x;

        CosFunction cosFunction = new CosFunction(sinStub);

        assertEquals(Math.PI / 2 - originalX,
                cosFunction.calculate(originalX, EPS),
                DELTA);
    }

    @Test
    @DisplayName("cos(x): должен корректно передавать отрицательный аргумент в преобразованном виде")
    void shouldPassShiftedNegativeArgumentToSinFunction() {
        final double originalX = -1.2;

        MathFunction sinStub = (x, eps) -> x;

        CosFunction cosFunction = new CosFunction(sinStub);

        assertEquals(Math.PI / 2 - originalX,
                cosFunction.calculate(originalX, EPS),
                DELTA);
    }

    @Test
    @DisplayName("Интеграция cos -> sin.csv: значения должны совпадать с cos на реальном SinFunction")
    void shouldCalculateCosUsingSinValuesFromCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);

        MathFunction cosWithCsvSin = new CosFunction(sinFromCsv);
        MathFunction cosWithRealSin = new CosFunction(new SinFunction());

        for (double t = SIN_CSV_START; t <= SIN_CSV_END; t += SIN_CSV_STEP) {
            double x = Math.PI / 2 - t;

            assertEquals(
                    cosWithRealSin.calculate(x, EPS),
                    cosWithCsvSin.calculate(x, EPS),
                    DELTA,
                    "Mismatch at x = " + x + ", shifted arg = " + t
            );
        }
    }

    @Test
    @DisplayName("Интеграция cos -> sin.csv: значения должны совпадать с эталонным Math.cos")
    void shouldMatchMathCosUsingSinValuesFromCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosWithCsvSin = new CosFunction(sinFromCsv);

        for (double t = SIN_CSV_START; t <= SIN_CSV_END; t += SIN_CSV_STEP) {
            double x = Math.PI / 2 - t;

            assertEquals(
                    Math.cos(x),
                    cosWithCsvSin.calculate(x, EPS),
                    DELTA,
                    "Mismatch at x = " + x + ", shifted arg = " + t
            );
        }
    }

    @AfterAll
    static void exportCosValuesToCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsvSin = new CosFunction(sinFromCsv);

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                cosFromCsvSin,
                COS_CSV_START,
                COS_CSV_END,
                COS_CSV_STEP,
                EPS,
                COS_CSV_FILE,
                "cos(X)"
        );
    }
}