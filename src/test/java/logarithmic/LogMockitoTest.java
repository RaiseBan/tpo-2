package logarithmic;

import org.example.functions.logarithmic.Ln;
import org.example.functions.logarithmic.Log;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogMockitoTest {

    private Ln lnMock;
    private Log log;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        lnMock = mock(Ln.class);
        log = new Log(lnMock, 10);
    }

    @Test
    void testLogCalculationWithMock() {
        // Подготовка данных
        double x = 100.0;
        double lnValue = 4.6052;
        double lnBase = 2.3026;

        // Настройка поведения моков
        when(lnMock.calculate(x, precision)).thenReturn(lnValue);
        when(lnMock.calculate(10, precision)).thenReturn(lnBase);

        // Ожидаемый результат: log_10(100) = ln(100) / ln(10) ≈ 4.6052 / 2.3026 ≈ 2
        double expected = lnValue / lnBase;

        // Вызов тестируемого метода
        double result = log.calculate(x, precision);

        System.out.println(result);

        // Проверка результата
        assertEquals(expected, result, 1e-4);

        // Проверка вызовов моков
        verify(lnMock).calculate(x, precision);
        verify(lnMock).calculate(10, precision);
    }

    @Test
    void testLogOfOne() {
        // log_b(1) = 0 для любого основания b
        double x = 1.0;

        // Настройка поведения моков
        when(lnMock.calculate(x, precision)).thenReturn(0.0);
        when(lnMock.calculate(10, precision)).thenReturn(2.3026);

        // Вызов тестируемого метода
        double result = log.calculate(x, precision);

        // Проверка результата
        assertEquals(0.0, result, 1e-4);
    }

    @Test
    void testLogOfBase() {
        // log_b(b) = 1 для любого основания b
        double x = 10.0; // основание 10

        // Настройка поведения моков
        when(lnMock.calculate(x, precision)).thenReturn(2.3026);
        when(lnMock.calculate(10, precision)).thenReturn(2.3026);

        // Вызов тестируемого метода
        double result = log.calculate(x, precision);

        // Проверка результата
        assertEquals(1.0, result, 1e-4);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -10.0})
    void testInvalidArguments(double x) {
        // Для отрицательных чисел и нуля логарифм не определен
        when(lnMock.calculate(x, precision)).thenReturn(Double.NaN);

        double result = log.calculate(x, precision);

        assertTrue(Double.isNaN(result));
        verify(lnMock).calculate(x, precision);
    }

    @Test
    void testConstructorWithInvalidBase() {
        // Основание логарифма не может быть <= 0 или = 1
        assertThrows(IllegalArgumentException.class, () -> new Log(lnMock, 0));
        assertThrows(IllegalArgumentException.class, () -> new Log(lnMock, -1));
        assertThrows(IllegalArgumentException.class, () -> new Log(lnMock, 1));
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор с основанием 10 работает корректно
        Log defaultLog = new Log(10);

        // log_10(100) должен быть примерно равен 2
        double result = defaultLog.calculate(100, precision);
        assertEquals(2.0, result, 1e-2);
    }

    @Test
    void testGetBase() {
        // Проверяем метод получения основания логарифма
        assertEquals(10.0, log.getBase());

        // Проверяем на другом основании
        Log log2 = new Log(lnMock, 2);
        assertEquals(2.0, log2.getBase());
    }

    @Test
    void testLnReturnsNaN() {
        double x = 5.0;

        // Имитируем ситуацию, когда ln возвращает NaN
        when(lnMock.calculate(x, precision)).thenReturn(Double.NaN);

        // Результат тоже должен быть NaN
        double result = log.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testLnBaseReturnsZero() {
        double x = 5.0;

        // Имитируем ситуацию, когда ln(base) возвращает 0
        when(lnMock.calculate(x, precision)).thenReturn(1.6094);
        when(lnMock.calculate(10, precision)).thenReturn(0.0);

        // Результат должен быть NaN (деление на ноль)
        double result = log.calculate(x, precision);

        assertTrue(Double.isNaN(result) || Double.isInfinite(result));
    }
}
