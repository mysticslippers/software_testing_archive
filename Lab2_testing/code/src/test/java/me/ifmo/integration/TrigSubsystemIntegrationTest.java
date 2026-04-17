package me.ifmo.integration;

import me.ifmo.function.MathFunction;
import me.ifmo.function.trig.CosFunction;
import me.ifmo.function.trig.CotFunction;
import me.ifmo.function.trig.CscFunction;
import me.ifmo.function.trig.SinFunction;
import me.ifmo.function.trig.TanFunction;
import me.ifmo.util.CsvExporter;
import me.ifmo.util.CsvMockitoUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrigSubsystemIntegrationTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = -100.0;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final double STABLE_SIN_THRESHOLD = 1e-2;
    private static final double STABLE_COS_THRESHOLD = 1e-2;

    private static final String SIN_CSV_FILE = "sin.csv";
    private static final String COS_CSV_FILE = "cos.csv";
    private static final String TAN_CSV_FILE = "tan.csv";
    private static final String COT_CSV_FILE = "cot.csv";
    private static final String CSC_CSV_FILE = "csc.csv";

    @BeforeAll
    static void ensureTrigCsvFilesExist() throws IOException {
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

        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);

        MathFunction tanFromCsvDeps = new TanFunction(sinFromCsv, cosFromCsv);
        exporter.export(
                tanFromCsvDeps,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                TAN_CSV_FILE,
                "tan(X)"
        );

        MathFunction cotFromCsvDeps = new CotFunction(sinFromCsv, cosFromCsv);
        exporter.export(
                cotFromCsvDeps,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                COT_CSV_FILE,
                "cot(X)"
        );

        MathFunction cscFromCsvDeps = new CscFunction(sinFromCsv);
        exporter.export(
                cscFromCsvDeps,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                CSC_CSV_FILE,
                "csc(X)"
        );
    }

    @Test
    @DisplayName("Интеграция тригонометрической подсистемы через CSV: значения должны совпадать с реальными реализациями")
    void shouldIntegrateWholeTrigSubsystemUsingCsv() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);
        MathFunction tanFromCsv = CsvMockitoUtil.mockFromCsv(TAN_CSV_FILE);
        MathFunction cotFromCsv = CsvMockitoUtil.mockFromCsv(COT_CSV_FILE);
        MathFunction cscFromCsv = CsvMockitoUtil.mockFromCsv(CSC_CSV_FILE);

        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction realTan = new TanFunction(realSin, realCos);
        MathFunction realCot = new CotFunction(realSin, realCos);
        MathFunction realCsc = new CscFunction(realSin);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            assertEquals(realSin.calculate(x, EPS), sinFromCsv.calculate(x, EPS), DELTA, "sin mismatch at x = " + x);
            assertEquals(realCos.calculate(x, EPS), cosFromCsv.calculate(x, EPS), DELTA, "cos mismatch at x = " + x);

            double expectedTan = realTan.calculate(x, EPS);
            double actualTan = tanFromCsv.calculate(x, EPS);
            if (Double.isNaN(expectedTan)) {
                assertTrue(Double.isNaN(actualTan), "Expected tan NaN at x = " + x);
            } else {
                assertEquals(expectedTan, actualTan, DELTA, "tan mismatch at x = " + x);
            }

            double expectedCot = realCot.calculate(x, EPS);
            double actualCot = cotFromCsv.calculate(x, EPS);
            if (Double.isNaN(expectedCot)) {
                assertTrue(Double.isNaN(actualCot), "Expected cot NaN at x = " + x);
            } else {
                assertEquals(expectedCot, actualCot, DELTA, "cot mismatch at x = " + x);
            }

            double expectedCsc = realCsc.calculate(x, EPS);
            double actualCsc = cscFromCsv.calculate(x, EPS);
            if (Double.isNaN(expectedCsc)) {
                assertTrue(Double.isNaN(actualCsc), "Expected csc NaN at x = " + x);
            } else {
                assertEquals(expectedCsc, actualCsc, DELTA, "csc mismatch at x = " + x);
            }
        }
    }

    @Test
    @DisplayName("Интеграция тригонометрической подсистемы через CSV: значения должны совпадать с эталонными функциями в устойчивых точках")
    void shouldMatchReferenceValuesUsingCsvTrigSubsystem() throws IOException {
        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);
        MathFunction tanFromCsv = CsvMockitoUtil.mockFromCsv(TAN_CSV_FILE);
        MathFunction cotFromCsv = CsvMockitoUtil.mockFromCsv(COT_CSV_FILE);
        MathFunction cscFromCsv = CsvMockitoUtil.mockFromCsv(CSC_CSV_FILE);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            assertEquals(Math.sin(x), sinFromCsv.calculate(x, EPS), DELTA, "sin mismatch at x = " + x);
            assertEquals(Math.cos(x), cosFromCsv.calculate(x, EPS), DELTA, "cos mismatch at x = " + x);

            if (Math.abs(Math.cos(x)) >= STABLE_COS_THRESHOLD) {
                double tanValue = tanFromCsv.calculate(x, EPS);
                if (!Double.isNaN(tanValue)) {
                    assertEquals(Math.tan(x), tanValue, ETALON_DELTA, "tan mismatch at x = " + x);
                }
            }

            if (Math.abs(Math.sin(x)) >= STABLE_SIN_THRESHOLD) {
                double cotValue = cotFromCsv.calculate(x, EPS);
                if (!Double.isNaN(cotValue)) {
                    assertEquals(Math.cos(x) / Math.sin(x), cotValue, ETALON_DELTA, "cot mismatch at x = " + x);
                }

                double cscValue = cscFromCsv.calculate(x, EPS);
                if (!Double.isNaN(cscValue)) {
                    assertEquals(1.0 / Math.sin(x), cscValue, ETALON_DELTA, "csc mismatch at x = " + x);
                }
            }
        }
    }
}