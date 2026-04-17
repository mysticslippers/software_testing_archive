package me.ifmo.integration;

import me.ifmo.function.MathFunction;
import me.ifmo.function.log.LnFunction;
import me.ifmo.function.log.Log10Function;
import me.ifmo.function.log.Log3Function;
import me.ifmo.util.CsvExporter;
import me.ifmo.util.CsvMockitoUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogSubsystemIntegrationTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double ETALON_DELTA = 1e-2;

    private static final double CSV_START = 0.25;
    private static final double CSV_END = 100.0;
    private static final double CSV_STEP = 0.25;

    private static final String LN_CSV_FILE = "ln.csv";
    private static final String LOG3_CSV_FILE = "log3.csv";
    private static final String LOG10_CSV_FILE = "log10.csv";

    @BeforeAll
    static void ensureLogCsvFilesExist() throws IOException {
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

        MathFunction lnFromCsv = CsvMockitoUtil.mockFromCsv(LN_CSV_FILE);

        MathFunction log3FromCsvDeps = new Log3Function(lnFromCsv);
        exporter.export(
                log3FromCsvDeps,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                LOG3_CSV_FILE,
                "log3(X)"
        );

        MathFunction log10FromCsvDeps = new Log10Function(lnFromCsv);
        exporter.export(
                log10FromCsvDeps,
                CSV_START,
                CSV_END,
                CSV_STEP,
                EPS,
                LOG10_CSV_FILE,
                "log10(X)"
        );
    }

    @Test
    @DisplayName("Интеграция логарифмической подсистемы через CSV: значения должны совпадать с реальными реализациями")
    void shouldIntegrateWholeLogSubsystemUsingCsv() throws IOException {
        MathFunction lnFromCsv = CsvMockitoUtil.mockFromCsv(LN_CSV_FILE);
        MathFunction log3FromCsv = CsvMockitoUtil.mockFromCsv(LOG3_CSV_FILE);
        MathFunction log10FromCsv = CsvMockitoUtil.mockFromCsv(LOG10_CSV_FILE);

        MathFunction realLn = new LnFunction();
        MathFunction realLog3 = new Log3Function(realLn);
        MathFunction realLog10 = new Log10Function(realLn);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            assertEquals(realLn.calculate(x, EPS), lnFromCsv.calculate(x, EPS), DELTA, "ln mismatch at x = " + x);
            assertEquals(realLog3.calculate(x, EPS), log3FromCsv.calculate(x, EPS), DELTA, "log3 mismatch at x = " + x);
            assertEquals(realLog10.calculate(x, EPS), log10FromCsv.calculate(x, EPS), DELTA, "log10 mismatch at x = " + x);
        }
    }

    @Test
    @DisplayName("Интеграция логарифмической подсистемы через CSV: значения должны совпадать с эталонными функциями")
    void shouldMatchReferenceValuesUsingCsvLogSubsystem() throws IOException {
        MathFunction lnFromCsv = CsvMockitoUtil.mockFromCsv(LN_CSV_FILE);
        MathFunction log3FromCsv = CsvMockitoUtil.mockFromCsv(LOG3_CSV_FILE);
        MathFunction log10FromCsv = CsvMockitoUtil.mockFromCsv(LOG10_CSV_FILE);

        for (double x = CSV_START; x <= CSV_END; x += CSV_STEP) {
            assertEquals(Math.log(x), lnFromCsv.calculate(x, EPS), DELTA, "ln mismatch at x = " + x);
            assertEquals(Math.log(x) / Math.log(3.0), log3FromCsv.calculate(x, EPS), ETALON_DELTA, "log3 mismatch at x = " + x);
            assertEquals(Math.log10(x), log10FromCsv.calculate(x, EPS), ETALON_DELTA, "log10 mismatch at x = " + x);
        }
    }
}