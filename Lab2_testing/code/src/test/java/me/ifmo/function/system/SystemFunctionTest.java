package me.ifmo.function.system;

import me.ifmo.function.MathFunction;
import me.ifmo.function.log.LnFunction;
import me.ifmo.function.log.Log10Function;
import me.ifmo.function.log.Log3Function;
import me.ifmo.function.trig.CosFunction;
import me.ifmo.function.trig.CotFunction;
import me.ifmo.function.trig.CscFunction;
import me.ifmo.function.trig.SinFunction;
import me.ifmo.function.trig.TanFunction;
import me.ifmo.util.CsvBackedMathFunction;
import me.ifmo.util.CsvExporter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemFunctionTest {

    private static final double EPS = 1e-6;
    private static final double DELTA = 1e-9;

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

    private static final String SYSTEM_CSV_FILE = "system.csv";

    @BeforeAll
    static void ensureCsvDependenciesExist() throws IOException {
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

        MathFunction sinFromCsv = new CsvBackedMathFunction(SIN_CSV_FILE);
        MathFunction cosFromCsv = new CsvBackedMathFunction(COS_CSV_FILE);

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

        // 3. Базовый логарифм
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

        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);

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
    @DisplayName("x <= 0: должна использоваться тригонометрическая ветка системы")
    void shouldUseTrigBranchForNonPositiveX() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> 1.0;
        MathFunction cotStub = (x, eps) -> 4.0;

        MathFunction lnStub = (x, eps) -> 100.0;
        MathFunction log3Stub = (x, eps) -> 100.0;
        MathFunction log10Stub = (x, eps) -> 100.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = Math.pow((2.0 + 1.0 + 4.0) / 2.0, 3) * 4.0 / Math.pow(4.0, 3);

        assertEquals(expected, systemFunction.calculate(0.0, EPS), DELTA);
    }

    @Test
    @DisplayName("x > 0: должна использоваться логарифмическая ветка системы")
    void shouldUseLogBranchForPositiveX() {
        MathFunction tanStub = (x, eps) -> 999.0;
        MathFunction cscStub = (x, eps) -> 999.0;
        MathFunction cotStub = (x, eps) -> 999.0;

        MathFunction lnStub = (x, eps) -> 2.0;
        MathFunction log3Stub = (x, eps) -> 5.0;
        MathFunction log10Stub = (x, eps) -> 3.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = ((((2.0 + 3.0) * 5.0) * (5.0 - 5.0)) - 5.0) + 5.0;

        assertEquals(expected, systemFunction.calculate(1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Тригонометрическая ветка: корректное вычисление по формуле на подставных значениях")
    void shouldCalculateTrigBranchUsingStubValues() {
        MathFunction tanStub = (x, eps) -> 1.0;
        MathFunction cscStub = (x, eps) -> 2.0;
        MathFunction cotStub = (x, eps) -> 2.0;

        MathFunction lnStub = (x, eps) -> 0.0;
        MathFunction log3Stub = (x, eps) -> 0.0;
        MathFunction log10Stub = (x, eps) -> 0.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = Math.pow((1.0 + 2.0 + 2.0) / 1.0, 3) * 2.0 / Math.pow(2.0, 3);

        assertEquals(expected, systemFunction.calculate(-1.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Логарифмическая ветка: корректное вычисление по формуле на подставных значениях")
    void shouldCalculateLogBranchUsingStubValues() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> 7.0;
        MathFunction log3Stub = (x, eps) -> 4.0;
        MathFunction log10Stub = (x, eps) -> 1.5;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        double expected = ((((7.0 + 1.5) * 4.0) * (4.0 - 4.0)) - 4.0) + 4.0;

        assertEquals(expected, systemFunction.calculate(2.0, EPS), DELTA);
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если tan(x) вернул NaN")
    void shouldReturnNaNWhenTanIsNaN() {
        MathFunction tanStub = (x, eps) -> Double.NaN;
        MathFunction cscStub = (x, eps) -> 2.0;
        MathFunction cotStub = (x, eps) -> 3.0;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если csc(x) вернул NaN")
    void shouldReturnNaNWhenCscIsNaN() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> Double.NaN;
        MathFunction cotStub = (x, eps) -> 3.0;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если cot(x) вернул NaN")
    void shouldReturnNaNWhenCotIsNaN() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> 3.0;
        MathFunction cotStub = (x, eps) -> Double.NaN;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Тригонометрическая ветка: должна возвращать NaN, если знаменатель |cot(x)^3| меньше eps")
    void shouldReturnNaNWhenTrigDenominatorIsTooSmall() {
        MathFunction tanStub = (x, eps) -> 2.0;
        MathFunction cscStub = (x, eps) -> 3.0;
        MathFunction cotStub = (x, eps) -> 0.001;

        MathFunction lnStub = (x, eps) -> 1.0;
        MathFunction log3Stub = (x, eps) -> 1.0;
        MathFunction log10Stub = (x, eps) -> 1.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(-1.0, EPS)));
    }

    @Test
    @DisplayName("Логарифмическая ветка: должна возвращать NaN, если ln(x) вернул NaN")
    void shouldReturnNaNWhenLnIsNaN() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> Double.NaN;
        MathFunction log3Stub = (x, eps) -> 2.0;
        MathFunction log10Stub = (x, eps) -> 3.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Логарифмическая ветка: должна возвращать NaN, если log3(x) вернул NaN")
    void shouldReturnNaNWhenLog3IsNaN() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> 2.0;
        MathFunction log3Stub = (x, eps) -> Double.NaN;
        MathFunction log10Stub = (x, eps) -> 3.0;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Логарифмическая ветка: должна возвращать NaN, если log10(x) вернул NaN")
    void shouldReturnNaNWhenLog10IsNaN() {
        MathFunction tanStub = (x, eps) -> 0.0;
        MathFunction cscStub = (x, eps) -> 0.0;
        MathFunction cotStub = (x, eps) -> 0.0;

        MathFunction lnStub = (x, eps) -> 2.0;
        MathFunction log3Stub = (x, eps) -> 4.0;
        MathFunction log10Stub = (x, eps) -> Double.NaN;

        SystemFunction systemFunction = new SystemFunction(
                tanStub,
                cscStub,
                cotStub,
                lnStub,
                log3Stub,
                log10Stub
        );

        assertTrue(Double.isNaN(systemFunction.calculate(1.0, EPS)));
    }

    @Test
    @DisplayName("Интеграция system -> trig.csv + log.csv: значения должны совпадать с системой на реальных зависимостях")
    void shouldCalculateSystemUsingCsvDependencies() throws IOException {
        MathFunction tanFromCsv = new CsvBackedMathFunction(TAN_CSV_FILE);
        MathFunction cscFromCsv = new CsvBackedMathFunction(CSC_CSV_FILE);
        MathFunction cotFromCsv = new CsvBackedMathFunction(COT_CSV_FILE);

        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log3FromCsv = new CsvBackedMathFunction(LOG3_CSV_FILE);
        MathFunction log10FromCsv = new CsvBackedMathFunction(LOG10_CSV_FILE);

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

    @AfterAll
    static void exportSystemValuesToCsv() throws IOException {
        MathFunction tanFromCsv = new CsvBackedMathFunction(TAN_CSV_FILE);
        MathFunction cscFromCsv = new CsvBackedMathFunction(CSC_CSV_FILE);
        MathFunction cotFromCsv = new CsvBackedMathFunction(COT_CSV_FILE);

        MathFunction lnFromCsv = new CsvBackedMathFunction(LN_CSV_FILE);
        MathFunction log3FromCsv = new CsvBackedMathFunction(LOG3_CSV_FILE);
        MathFunction log10FromCsv = new CsvBackedMathFunction(LOG10_CSV_FILE);

        MathFunction systemFromCsvDeps = new SystemFunction(
                tanFromCsv,
                cscFromCsv,
                cotFromCsv,
                lnFromCsv,
                log3FromCsv,
                log10FromCsv
        );

        CsvExporter exporter = new CsvExporter();
        exporter.export(
                systemFromCsvDeps,
                TRIG_CSV_START,
                TRIG_CSV_END,
                TRIG_CSV_STEP,
                EPS,
                SYSTEM_CSV_FILE,
                "system(X)"
        );
    }
}