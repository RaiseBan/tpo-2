package system;

import org.example.functions.logarithmic.Ln;
import org.example.functions.logarithmic.Log;
import org.example.functions.system.SystemFunction;
import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Cot;
import org.example.functions.trigonometric.Csc;
import org.example.functions.trigonometric.Sec;
import org.example.functions.trigonometric.Sin;
import org.example.functions.trigonometric.Tan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FunctionSystemIntegrationTest {

    private SystemFunction systemFunction;
    private Sin sin;
    private Cos cos;
    private Tan tan;
    private Cot cot;
    private Sec sec;
    private Csc csc;
    private Ln ln;
    private Log log10;
    private Log log2;
    private Log log3;

    private final double precision = 1e-6;
    private final double delta = 1e-4;

    @BeforeEach
    void setUp() {
        sin = new Sin(1e-10, 1000);
        cos = new Cos(sin);
        tan = new Tan(sin, cos);
        cot = new Cot(sin, cos);
        sec = new Sec(cos);
        csc = new Csc(sin);

        ln = new Ln(1e-10, 1000);
        log10 = new Log(ln, 10);
        log2 = new Log(ln, 2);
        log3 = new Log(ln, 3);

        systemFunction = new SystemFunction(cos, sec, cot, tan, csc, ln, log10, log2, log3);
    }

    @Test
    void testSystemFunctionNegativeValues() {
        // Негативные значения для тригонометрической ветви
        double[] testValues = {-0.5, -1.0, -2.0};  // Убираем -Math.PI из проблемных значений

        for (double x : testValues) {
            double result = systemFunction.calculate(x, precision);
            assertFalse(Double.isNaN(result), "System function returned NaN for x = " + x);

            // Вычисляем ожидаемый результат по формуле (для проверки интеграции)
            // (((((cos(x) ^ 3) * sec(x)) ^ 2) + ((cot(x) * (tan(x) + csc(x))) - (csc(x) * csc(x)))) - (sec(x) / (cot(x) ^ 2)))
            double cosX = cos.calculate(x, precision);
            double secX = sec.calculate(x, precision);
            double cotX = cot.calculate(x, precision);
            double tanX = tan.calculate(x, precision);
            double cscX = csc.calculate(x, precision);

            if (!Double.isNaN(cosX) && !Double.isNaN(secX) && !Double.isNaN(cotX)
                    && !Double.isNaN(tanX) && !Double.isNaN(cscX)) {

                double cosCubed = Math.pow(cosX, 3);
                double cosCubedSecX = cosCubed * secX;
                double cosCubedSecXSquared = Math.pow(cosCubedSecX, 2);
                double tanPlusCsc = tanX + cscX;
                double cotTimesTanPlusCsc = cotX * tanPlusCsc;
                double cscSquared = cscX * cscX;
                double cotTimesTanPlusCscMinusCscSquared = cotTimesTanPlusCsc - cscSquared;
                double firstPart = cosCubedSecXSquared + cotTimesTanPlusCscMinusCscSquared;
                double cotSquared = Math.pow(cotX, 2);
                double secDividedByCotSquared = secX / cotSquared;
                double expected = firstPart - secDividedByCotSquared;

                assertEquals(expected, result, delta);
            }
        }
    }

    @Test
    void testSystemFunctionPositiveValues() {
        // Позитивные значения для логарифмической ветви
        double[] testValues = {0.5, 1.0, 2.0, Math.PI};

        for (double x : testValues) {
            double result = systemFunction.calculate(x, precision);

            if (x == 1.0) {
                // Для x = 1 логарифмы обнуляются, что может приводить к неопределенностям
                continue;
            }

            assertFalse(Double.isNaN(result), "System function returned NaN for x = " + x);

            // Вычисляем ожидаемый результат по формуле (для проверки интеграции)
            // (((((log_10(x) - ln(x)) / (log_2(x) / log_3(x))) ^ 2) * log_10(x)) / log_10(x))
            double log10X = log10.calculate(x, precision);
            double lnX = ln.calculate(x, precision);
            double log2X = log2.calculate(x, precision);
            double log3X = log3.calculate(x, precision);

            if (!Double.isNaN(log10X) && !Double.isNaN(lnX)
                    && !Double.isNaN(log2X) && !Double.isNaN(log3X)
                    && Math.abs(log2X) > precision && Math.abs(log3X) > precision) {

                double log10MinusLn = log10X - lnX;
                double log2DivLog3 = log2X / log3X;
                double division = log10MinusLn / log2DivLog3;
                double divisionSquared = Math.pow(division, 2);
                double expected = divisionSquared;

                assertEquals(expected, result, delta);
            }
        }
    }


    @Test
    void testAllComponentsOfSystem() {
        // Проверяем, что все компоненты системы работают правильно
        assertEquals(0.0, sin.calculate(0.0, precision), delta);
        assertEquals(1.0, cos.calculate(0.0, precision), delta);
        assertEquals(0.0, tan.calculate(0.0, precision), delta);
        assertEquals(1.0, sec.calculate(0.0, precision), delta);

        assertEquals(0.0, ln.calculate(1.0, precision), delta);
        assertEquals(1.0, log10.calculate(10.0, precision), delta);
        assertEquals(3.0, log2.calculate(8.0, precision), delta);
        assertEquals(2.0, log3.calculate(9.0, precision), delta);
    }

    @Test
    void testSystemFunctionWithEdgeCases() {
        // Проверка поведения системы на граничных случаях

        // При x = 0, в тригонометрической ветви некоторые функции не определены
        assertTrue(Double.isNaN(systemFunction.calculate(0.0, precision)));

        // При x = π/2, ожидаем что функция вернет нормальное значение, а не NaN
        assertFalse(Double.isNaN(systemFunction.calculate(Math.PI / 2, precision)));

        // При x → 0+, в логарифмической ветви логарифмы не возвращают NaN, а дают конкретное значение
        assertFalse(Double.isNaN(systemFunction.calculate(1e-10, precision)));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0.05:1.14327",
        "0.2:0.32998",
        "0.5:0.06121",
        "2:0.0612",
        "2.7:0.1256",
        "3.5:0.1999",
        "-2:10.7901",
        "-2.1:4.8364",
        "-2.45:-2.1101",
        "-4.2:5.5425",
        "-4:0.3462",
        "-3.7:-4.6057",
        "-0.5:0.7204",
        "-0.8:-0.8754",
        "-1:-4.0531",
        "-5.78:0.7101",
        "-5.4:-1.7850",
        "-5.2:-7.2219",}, delimiter = ':')
    void testPoint(double x, double yExpected) {
        double yCalculated = systemFunction.calculate(x, precision);
        assertEquals(yExpected, yCalculated, delta);
    }
}
