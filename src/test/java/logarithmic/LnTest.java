package logarithmic;

import org.example.functions.logarithmic.Ln;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class LnTest {

    private Ln ln;
    private final double precision = 1e-6;
    private final double delta = 1e-4;

    @BeforeEach
    void setUp() {
        ln = new Ln(1e-10, 500);
    }

    @Test
    void testLnOfOne() {
        assertEquals(0.0, ln.calculate(1.0, precision), delta);
    }

    @Test
    void testLnOfE() {
        assertEquals(1.0, ln.calculate(Math.E, precision), delta);
    }

    @Test
    void testLnOfTen() {
        assertEquals(2.3026, ln.calculate(10.0, precision), delta);
    }

    @Test
    void testLnOfTwo() {
        assertEquals(0.6931, ln.calculate(2.0, precision), delta);
    }

    @Test
    void testLnOfOneHalf() {
        assertEquals(-0.6931, ln.calculate(0.5, precision), delta);
    }

    @Test
    void testLnOfHundred() {
        assertEquals(4.6052, ln.calculate(100.0, precision), delta);
    }

    @Test
    void testLnOfVeryLargeNumber() {
        assertEquals(23.0259, ln.calculate(1e10, precision), delta);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -5.0, -10.0})
    void testLnOfInvalidArguments(double x) {
        assertTrue(Double.isNaN(ln.calculate(x, precision)));
    }

    @Test
    void testLnOfLargeValue() {
        double result = ln.calculate(1e9, precision);
        assertTrue(result > 0);
        assertFalse(Double.isNaN(result));
        assertFalse(Double.isInfinite(result));
    }

    @Test
    void testLnPrecision() {
        Ln lowPrecisionLn = new Ln(1e-3, 10);
        Ln highPrecisionLn = new Ln(1e-10, 1000);

        double x = 2.5;
        double lowPrecisionResult = lowPrecisionLn.calculate(x, 1e-3);
        double highPrecisionResult = highPrecisionLn.calculate(x, 1e-10);

        assertNotEquals(lowPrecisionResult, highPrecisionResult, 1e-6);
    }

    @Test
    void testLnProperties() {
        // Проверка свойства ln(a*b) = ln(a) + ln(b)
        double a = 2.0;
        double b = 3.0;

        double lnA = ln.calculate(a, precision);
        double lnB = ln.calculate(b, precision);
        double lnAB = ln.calculate(a * b, precision);

        assertEquals(lnA + lnB, lnAB, delta);
    }
}
