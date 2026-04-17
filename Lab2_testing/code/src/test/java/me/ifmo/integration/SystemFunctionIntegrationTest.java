package me.ifmo.integration;

import me.ifmo.function.MathFunction;
import me.ifmo.function.log.LnFunction;
import me.ifmo.function.log.Log10Function;
import me.ifmo.function.log.Log3Function;
import me.ifmo.function.system.SystemFunction;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemFunctionIntegrationTest {

    private static final double EPS = 1e-6;

    private static final double CSV_DELTA = 1e-2;
    private static final double RELATIVE_DELTA = 1e-4;

    private static final double TRIG_CSV_START = -100.0;
    private static final double TRIG_CSV_END = 100.0;
    private static final double TRIG_CSV_STEP = 0.25;

    private static final double LOG_CSV_START = 0.25;
    private static final double LOG_CSV_END = 100.0;
    private static final double LOG_CSV_STEP = 0.25;

    private static final String SIN_CSV_FILE = "sin.csv";
    private static final String COS_CSV_FILE = "cos.csv";
    private static final String TAN_CSV_FILE = "tan.csv";
    private static final String COT_CSV_FILE = "cot.csv";
    private static final String CSC_CSV_FILE = "csc.csv";

    private static final String LN_CSV_FILE = "ln.csv";
    private static final String LOG3_CSV_FILE = "log3.csv";
    private static final String LOG10_CSV_FILE = "log10.csv";

    @BeforeAll
    static void ensureAllCsvFilesExist() throws IOException {
        CsvExporter exporter = new CsvExporter();

        MathFunction realSin = new SinFunction();
        exporter.export(
                realSin,
                TRIG_CSV_START,
                TRIG_CSV_END,
                TRIG_CSV_STEP,
                EPS,
                SIN_CSV_FILE,
                "sin(X)"
        );

        MathFunction realCos = new CosFunction(realSin);
        exporter.export(
                realCos,
                TRIG_CSV_START,
                TRIG_CSV_END,
                TRIG_CSV_STEP,
                EPS,
                COS_CSV_FILE,
                "cos(X)"
        );

        MathFunction sinFromCsv = CsvMockitoUtil.mockFromCsv(SIN_CSV_FILE);
        MathFunction cosFromCsv = CsvMockitoUtil.mockFromCsv(COS_CSV_FILE);

        MathFunction tanFromCsvDeps = new TanFunction(sinFromCsv, cosFromCsv);
        exporter.export(
                tanFromCsvDeps,
                TRIG_CSV_START,
                TRIG_CSV_END,
                TRIG_CSV_STEP,
                EPS,
                TAN_CSV_FILE,
                "tan(X)"
        );

        MathFunction cotFromCsvDeps = new CotFunction(sinFromCsv, cosFromCsv);
        exporter.export(
                cotFromCsvDeps,
                TRIG_CSV_START,
                TRIG_CSV_END,
                TRIG_CSV_STEP,
                EPS,
                COT_CSV_FILE,
                "cot(X)"
        );

        MathFunction cscFromCsvDeps = new CscFunction(sinFromCsv);
        exporter.export(
                cscFromCsvDeps,
                TRIG_CSV_START,
                TRIG_CSV_END,
                TRIG_CSV_STEP,
                EPS,
                CSC_CSV_FILE,
                "csc(X)"
        );

        MathFunction realLn = new LnFunction();
        exporter.export(
                realLn,
                LOG_CSV_START,
                LOG_CSV_END,
                LOG_CSV_STEP,
                EPS,
                LN_CSV_FILE,
                "ln(X)"
        );

        MathFunction lnFromCsv = CsvMockitoUtil.mockFromCsv(LN_CSV_FILE);

        MathFunction log3FromCsvDeps = new Log3Function(lnFromCsv);
        exporter.export(
                log3FromCsvDeps,
                LOG_CSV_START,
                LOG_CSV_END,
                LOG_CSV_STEP,
                EPS,
                LOG3_CSV_FILE,
                "log3(X)"
        );

        MathFunction log10FromCsvDeps = new Log10Function(lnFromCsv);
        exporter.export(
                log10FromCsvDeps,
                LOG_CSV_START,
                LOG_CSV_END,
                LOG_CSV_STEP,
                EPS,
                LOG10_CSV_FILE,
                "log10(X)"
        );
    }

    @Test
    @DisplayName("Интеграция полной системы через CSV: значения должны совпадать с системой на реальных зависимостях")
    void shouldIntegrateWholeSystemUsingCsv() throws IOException {
        MathFunction tanFromCsv = CsvMockitoUtil.mockFromCsv(TAN_CSV_FILE);
        MathFunction cscFromCsv = CsvMockitoUtil.mockFromCsv(CSC_CSV_FILE);
        MathFunction cotFromCsv = CsvMockitoUtil.mockFromCsv(COT_CSV_FILE);

        MathFunction lnFromCsv = CsvMockitoUtil.mockFromCsv(LN_CSV_FILE);
        MathFunction log3FromCsv = CsvMockitoUtil.mockFromCsv(LOG3_CSV_FILE);
        MathFunction log10FromCsv = CsvMockitoUtil.mockFromCsv(LOG10_CSV_FILE);

        MathFunction systemWithCsvDeps = new SystemFunction(
                tanFromCsv,
                cscFromCsv,
                cotFromCsv,
                lnFromCsv,
                log3FromCsv,
                log10FromCsv
        );

        MathFunction realSin = new SinFunction();
        MathFunction realCos = new CosFunction(realSin);
        MathFunction realTan = new TanFunction(realSin, realCos);
        MathFunction realCot = new CotFunction(realSin, realCos);
        MathFunction realCsc = new CscFunction(realSin);

        MathFunction realLn = new LnFunction();
        MathFunction realLog3 = new Log3Function(realLn);
        MathFunction realLog10 = new Log10Function(realLn);

        MathFunction systemWithRealDeps = new SystemFunction(
                realTan,
                realCsc,
                realCot,
                realLn,
                realLog3,
                realLog10
        );

        for (double x = TRIG_CSV_START; x <= TRIG_CSV_END; x += TRIG_CSV_STEP) {
            if (x > 0 && x < LOG_CSV_START) {
                continue;
            }

            double expected = systemWithRealDeps.calculate(x, EPS);
            double actual = systemWithCsvDeps.calculate(x, EPS);

            if (Double.isNaN(expected)) {
                assertTrue(Double.isNaN(actual), "Expected NaN at x = " + x);
            } else {
                double tolerance = Math.max(CSV_DELTA, Math.abs(expected) * RELATIVE_DELTA);
                assertEquals(expected, actual, tolerance, "Mismatch at x = " + x);
            }
        }
    }

    @Test
    @DisplayName("Интеграция полной системы через CSV: система должна возвращать корректные числовые значения в допустимых точках")
    void shouldReturnFiniteValuesForValidPointsUsingCsv() throws IOException {
        MathFunction tanFromCsv = CsvMockitoUtil.mockFromCsv(TAN_CSV_FILE);
        MathFunction cscFromCsv = CsvMockitoUtil.mockFromCsv(CSC_CSV_FILE);
        MathFunction cotFromCsv = CsvMockitoUtil.mockFromCsv(COT_CSV_FILE);

        MathFunction lnFromCsv = CsvMockitoUtil.mockFromCsv(LN_CSV_FILE);
        MathFunction log3FromCsv = CsvMockitoUtil.mockFromCsv(LOG3_CSV_FILE);
        MathFunction log10FromCsv = CsvMockitoUtil.mockFromCsv(LOG10_CSV_FILE);

        MathFunction systemWithCsvDeps = new SystemFunction(
                tanFromCsv,
                cscFromCsv,
                cotFromCsv,
                lnFromCsv,
                log3FromCsv,
                log10FromCsv
        );

        double trigResult = systemWithCsvDeps.calculate(-0.5, EPS);
        assertFalse(Double.isNaN(trigResult));

        double logResult = systemWithCsvDeps.calculate(3.0, EPS);
        assertFalse(Double.isNaN(logResult));
        assertEquals(0.0, logResult, CSV_DELTA);
    }
}