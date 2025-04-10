package trigonometric;

import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Cot;
import org.example.functions.trigonometric.Csc;
import org.example.functions.trigonometric.Sec;
import org.example.functions.trigonometric.Sin;
import org.example.functions.trigonometric.Tan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TrigonometricFunctionsTest {

    private Sin sin;
    private Cos cos;
    private Tan tan;
    private Cot cot;
    private Sec sec;
    private Csc csc;
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
    }

    @Test
    void testSinCosIntegration() {
        // Проверка основного тригонометрического тождества: sin²(x) + cos²(x) = 1
        double[] angles = {0, Math.PI / 6, Math.PI / 4, Math.PI / 3, Math.PI / 2, Math.PI, 3 * Math.PI / 2, 2 * Math.PI};

        for (double x : angles) {
            double sinX = sin.calculate(x, precision);
            double cosX = cos.calculate(x, precision);
            assertEquals(1.0, sinX * sinX + cosX * cosX, delta);
        }
    }

    @Test
    void testTanCotIntegration() {
        // Проверка тождества: tan(x) * cot(x) = 1 для углов, где оба определены
        double[] angles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, 2 * Math.PI / 3, 3 * Math.PI / 4, 5 * Math.PI / 6};

        for (double x : angles) {
            double tanX = tan.calculate(x, precision);
            double cotX = cot.calculate(x, precision);
            assertEquals(1.0, tanX * cotX, delta);
        }
    }

    @Test
    void testSecCscIntegration() {
        // Проверка определений: sec(x) = 1/cos(x) и csc(x) = 1/sin(x)
        double[] angles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, 2 * Math.PI / 3, 3 * Math.PI / 4, 5 * Math.PI / 6};

        for (double x : angles) {
            double cosX = cos.calculate(x, precision);
            double secX = sec.calculate(x, precision);
            assertEquals(1.0 / cosX, secX, delta);

            double sinX = sin.calculate(x, precision);
            double cscX = csc.calculate(x, precision);
            assertEquals(1.0 / sinX, cscX, delta);
        }
    }

    @ParameterizedTest
    @CsvSource({
        "0.0, 0.0, 1.0, 0.0, 1.0, Double.NaN",
        "0.785398, 0.7071, 0.7071, 1.0, 1.4142, 1.4142",
        "1.570796, 1.0, 0.0, Double.NaN, Double.NaN, 1.0",
        "3.141593, 0.0, -1.0, 0.0, -1.0, Double.NaN"
    })
    void testAllTrigFunctionsIntegration(double angle, double sinExpected, double cosExpected,
            String tanExpected, String secExpected, String cscExpected) {
        // Проверка всех тригонометрических функций для заданных углов
        double sinResult = sin.calculate(angle, precision);
        double cosResult = cos.calculate(angle, precision);
        double tanResult = tan.calculate(angle, precision);
        double secResult = sec.calculate(angle, precision);
        double cscResult = csc.calculate(angle, precision);

        assertEquals(sinExpected, sinResult, delta);
        assertEquals(cosExpected, cosResult, delta);

        if (tanExpected.equals("Double.NaN")) {
            assertTrue(Double.isNaN(tanResult));
        } else {
            assertEquals(Double.parseDouble(tanExpected), tanResult, delta);
        }

        if (secExpected.equals("Double.NaN")) {
            assertTrue(Double.isNaN(secResult));
        } else {
            assertEquals(Double.parseDouble(secExpected), secResult, delta);
        }

        if (cscExpected.equals("Double.NaN")) {
            assertTrue(Double.isNaN(cscResult));
        } else {
            assertEquals(Double.parseDouble(cscExpected), cscResult, delta);
        }
    }

    @Test
    void testSinCosComplementaryAngles() {
        // Проверка тождества: sin(x) = cos(π/2 - x)
        double[] angles = {0, Math.PI / 6, Math.PI / 4, Math.PI / 3, Math.PI / 2};

        for (double x : angles) {
            double sinX = sin.calculate(x, precision);
            double cosComplementary = cos.calculate(Math.PI / 2 - x, precision);
            assertEquals(sinX, cosComplementary, delta);
        }
    }

    @Test
    void testTanDefinition() {
        // Проверка определения: tan(x) = sin(x) / cos(x)
        double[] angles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, 2 * Math.PI / 3, 3 * Math.PI / 4, 5 * Math.PI / 6};

        for (double x : angles) {
            double sinX = sin.calculate(x, precision);
            double cosX = cos.calculate(x, precision);
            double tanX = tan.calculate(x, precision);

            assertEquals(sinX / cosX, tanX, delta);
        }
    }

    @Test
    void testCotDefinition() {
        // Проверка определения: cot(x) = cos(x) / sin(x)
        double[] angles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, 2 * Math.PI / 3, 3 * Math.PI / 4, 5 * Math.PI / 6};

        for (double x : angles) {
            double sinX = sin.calculate(x, precision);
            double cosX = cos.calculate(x, precision);
            double cotX = cot.calculate(x, precision);

            assertEquals(cosX / sinX, cotX, delta);
        }
    }

    @Test
    void testPythagoreanIdentities() {
        // Проверка тождеств: tan²(x) + 1 = sec²(x) и 1 + cot²(x) = csc²(x)
        double[] angles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, 2 * Math.PI / 3, 3 * Math.PI / 4, 5 * Math.PI / 6};

        for (double x : angles) {
            double tanX = tan.calculate(x, precision);
            double secX = sec.calculate(x, precision);
            assertEquals(Math.pow(tanX, 2) + 1, Math.pow(secX, 2), delta);

            double cotX = cot.calculate(x, precision);
            double cscX = csc.calculate(x, precision);
            assertEquals(1 + Math.pow(cotX, 2), Math.pow(cscX, 2), delta);
        }
    }
}
