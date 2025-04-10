package trigonometric;

import org.example.functions.trigonometric.Sin;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SinTest {

    private Sin sin;
    private final double precision = 1e-6;
    private final double delta = 1e-4;

    @BeforeEach
    void setUp() {
        sin = new Sin(1e-8, 200);
    }

    @Test
    void testSinOfZero() {
        assertEquals(0.0, sin.calculate(0.0, precision), delta);
    }

    @Test
    void testSinOfPiHalf() {
        assertEquals(1.0, sin.calculate(Math.PI / 2, precision), delta);
    }

    @Test
    void testSinOfPi() {
        assertEquals(0.0, sin.calculate(Math.PI, precision), delta);
    }

    @Test
    void testSinOfThreePiHalf() {
        assertEquals(-1.0, sin.calculate(3 * Math.PI / 2, precision), delta);
    }

    @Test
    void testSinOfTwoPi() {
        assertEquals(0.0, sin.calculate(2 * Math.PI, precision), delta);
    }

    @Test
    void testSinOfMinusPiHalf() {
        assertEquals(-1.0, sin.calculate(-Math.PI / 2, precision), delta);
    }

    @Test
    void testSinOfMinusPi() {
        assertEquals(0.0, sin.calculate(-Math.PI, precision), delta);
    }

    @Test
    void testSinOfPiQuarter() {
        assertEquals(0.7071, sin.calculate(Math.PI / 4, precision), delta);
    }

    @Test
    void testSinOfThreePiQuarter() {
        assertEquals(0.7071, sin.calculate(3 * Math.PI / 4, precision), delta);
    }

    @Test
    void testSinOfFivePiQuarter() {
        assertEquals(-0.7071, sin.calculate(5 * Math.PI / 4, precision), delta);
    }

    @Test
    void testSinOfSevenPiQuarter() {
        assertEquals(-0.7071, sin.calculate(7 * Math.PI / 4, precision), delta);
    }

    @Test
    void testSinOfLargeArgument() {
        double expected = sin.calculate(Math.PI / 6, precision);
        double actual = sin.calculate(Math.PI / 6 + 20 * Math.PI, precision);
        assertEquals(expected, actual, delta);
    }

    @Test
    void testSinOfNegativeLargeArgument() {
        double expected = sin.calculate(-Math.PI / 3, precision);
        double actual = sin.calculate(-Math.PI / 3 - 20 * Math.PI, precision);
        assertEquals(expected, actual, delta);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10.0, -5.0, -1.0, 0.0, 1.0, 5.0, 10.0})
    void testSinInRange(double x) {
        double result = sin.calculate(x, precision);
        assertTrue(result >= -1.0 && result <= 1.0);
    }

    @Test
    void testSinPrecision() {
        Sin lowPrecisionSin = new Sin(1e-2, 10);
        Sin highPrecisionSin = new Sin(1e-10, 500);

        double x = Math.PI / 3;
        double lowPrecisionResult = lowPrecisionSin.calculate(x, 1e-2);
        double highPrecisionResult = highPrecisionSin.calculate(x, 1e-10);

        assertNotEquals(lowPrecisionResult, highPrecisionResult, 1e-8);
    }
}
