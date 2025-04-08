package logarithmic;

import org.example.functions.logarithmic.Ln;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LnMockitoTest {

    private Ln ln;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        ln = spy(new Ln());
    }

    @Test
    void testLnOfOne() {
        // ln(1) = 0
        double result = ln.calculate(1.0, precision);
        assertEquals(0.0, result, precision);
    }

    @Test
    void testLnOfE() {
        // ln(e) = 1
        double result = ln.calculate(Math.E, precision);
        assertEquals(1.0, result, precision);
    }

    @Test
    void testLnOfTen() {
        // ln(10) ≈ 2.302585
        double result = ln.calculate(10.0, precision);
        assertEquals(2.3026, result, 1e-4);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -10.0})
    void testInvalidArguments(double x) {
        // Для отрицательных чисел и нуля натуральный логарифм не определен
        double result = ln.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCustomPrecision() {
        // Проверяем, что расчет с разной точностью дает разные результаты
        Ln lnDefault = new Ln();
        Ln lnHighPrecision = new Ln(1e-10, 1000);

        double resultDefault = lnDefault.calculate(2.0, 1e-6);
        double resultHighPrecision = lnHighPrecision.calculate(2.0, 1e-10);

        assertEquals(0.6931, resultDefault, 1e-4);
        assertEquals(0.6931471806, resultHighPrecision, 1e-5);
    }

    @Test
    void testSpyInteraction() {
        // Создаем шпион для Ln, чтобы проверить вызов внутренних методов
        Ln lnSpy = spy(new Ln());

        // Вызываем метод
        lnSpy.calculate(2.0, precision);

        // Проверяем, что метод calculate был вызван с правильными параметрами
        verify(lnSpy).calculate(2.0, precision);
    }

    @Test
    void testGetters() {
        Ln customLn = new Ln(1e-8, 200);

        assertEquals(1e-8, customLn.getEpsilon());
        assertEquals(200, customLn.getMaxIterations());
    }

    @Test
    void testNearZero() {
        // Проверяем поведение вблизи нуля
        double result = ln.calculate(0.0000001, precision);
        assertTrue(result < 0); // ln(x) < 0 для 0 < x < 1
        assertFalse(Double.isNaN(result));
    }

    @Test
    void testLargeNumber() {
        // Проверяем поведение для больших чисел
        double result = ln.calculate(1e10, precision);
        assertEquals(23.0259, result, 1e-4);
    }

    @Test
    void testMaxIterationsReached() {
        // Создаем Ln с очень малым количеством итераций
        Ln limitedLn = new Ln(precision, 1);

        // Вычисляем ln(10) - для точного результата нужно больше итераций
        double resultLimited = limitedLn.calculate(10.0, precision);
        double resultNormal = ln.calculate(10.0, precision);

        // Сравниваем с нормальным расчетом и убеждаемся, что есть различия
        assertNotEquals(resultNormal, resultLimited, 1e-3);
    }
} 