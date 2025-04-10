package logarithmic;

import org.example.functions.logarithmic.Ln;
import org.example.functions.logarithmic.Log;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LogarithmicFunctionsTest {

    private Ln ln;
    private Log log10;
    private Log log2;
    private Log log3;
    private Log log5;
    private final double precision = 1e-6;
    private final double delta = 1e-4;

    @BeforeEach
    void setUp() {
        ln = new Ln(1e-10, 1000);
        log10 = new Log(ln, 10);
        log2 = new Log(ln, 2);
        log3 = new Log(ln, 3);
        log5 = new Log(ln, 5);
    }

    @Test
    void testIntegrationWithNaturalLogarithm() {
        // Проверка, что ln(x) корректно используется в логарифмах
        assertEquals(0.0, ln.calculate(1.0, precision), delta);
        assertEquals(0.0, log10.calculate(1.0, precision), delta);
        assertEquals(0.0, log2.calculate(1.0, precision), delta);
    }

    @Test
    void testIntegrationWithLog10() {
        // Проверка log10 для разных значений
        assertEquals(1.0, log10.calculate(10.0, precision), delta);
        assertEquals(2.0, log10.calculate(100.0, precision), delta);
        assertEquals(3.0, log10.calculate(1000.0, precision), delta);
    }

    @Test
    void testIntegrationWithLog2() {
        // Проверка log2 для разных значений
        assertEquals(1.0, log2.calculate(2.0, precision), delta);
        assertEquals(2.0, log2.calculate(4.0, precision), delta);
        assertEquals(3.0, log2.calculate(8.0, precision), delta);
        assertEquals(4.0, log2.calculate(16.0, precision), delta);
    }

    @Test
    void testIntegrationWithLog3() {
        // Проверка log3 для разных значений
        assertEquals(1.0, log3.calculate(3.0, precision), delta);
        assertEquals(2.0, log3.calculate(9.0, precision), delta);
        assertEquals(3.0, log3.calculate(27.0, precision), delta);
        assertEquals(4.0, log3.calculate(81.0, precision), delta);
    }

    @Test
    void testIntegrationWithLog5() {
        // Проверка log5 для разных значений
        assertEquals(1.0, log5.calculate(5.0, precision), delta);
        assertEquals(2.0, log5.calculate(25.0, precision), delta);
        assertEquals(3.0, log5.calculate(125.0, precision), delta);
    }

    @ParameterizedTest
    @CsvSource({
        "2, 8, 3.0",
        "3, 27, 3.0",
        "10, 1000, 3.0"
    })
    void testLogIdentities(double base, double x, double expected) {
        Log log = new Log(ln, base);
        assertEquals(expected, log.calculate(x, precision), delta);
    }

    @Test
    void testLogarithmChangeOfBase() {
        // Проверка формулы изменения основания: log_a(x) = log_b(x) / log_b(a)
        double x = 16.0;

        // log_2(16) = 4
        double log2_x = log2.calculate(x, precision);

        // log_10(16) / log_10(2)
        double log10_x = log10.calculate(x, precision);
        double log10_2 = log10.calculate(2, precision);

        assertEquals(log2_x, log10_x / log10_2, delta);
    }

    @Test
    void testLogOfPower() {
        // Проверка свойства log_a(x^n) = n * log_a(x)
        double x = 2.0;
        double n = 3.0;

        // log_10(2^3) = log_10(8)
        double log10_x_pow_n = log10.calculate(Math.pow(x, n), precision);

        // 3 * log_10(2)
        double n_times_log10_x = n * log10.calculate(x, precision);

        assertEquals(log10_x_pow_n, n_times_log10_x, delta);
    }

    @Test
    void testLogOfProduct() {
        // Проверка свойства log_a(x*y) = log_a(x) + log_a(y)
        double x = 2.0;
        double y = 3.0;

        // log_10(2*3) = log_10(6)
        double log10_x_times_y = log10.calculate(x * y, precision);

        // log_10(2) + log_10(3)
        double log10_x_plus_log10_y = log10.calculate(x, precision) + log10.calculate(y, precision);

        assertEquals(log10_x_times_y, log10_x_plus_log10_y, delta);
    }

    @Test
    void testLogOfDivision() {
        // Проверка свойства log_a(x/y) = log_a(x) - log_a(y)
        double x = 8.0;
        double y = 2.0;

        // log_10(8/2) = log_10(4)
        double log10_x_div_y = log10.calculate(x / y, precision);

        // log_10(8) - log_10(2)
        double log10_x_minus_log10_y = log10.calculate(x, precision) - log10.calculate(y, precision);

        assertEquals(log10_x_div_y, log10_x_minus_log10_y, delta);
    }
}
