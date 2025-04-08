package trigonometric;

import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Sin;
import org.example.functions.trigonometric.Tan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TanMockitoTest {

    private Sin sinMock;
    private Cos cosMock;
    private Tan tan;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        sinMock = mock(Sin.class);
        cosMock = mock(Cos.class);
        tan = new Tan(sinMock, cosMock);
    }

    @Test
    void testTanViaCalculate() {
        // tan(x) = sin(x) / cos(x)
        double x = 1.0;

        // Настраиваем моки
        when(sinMock.calculate(x, precision)).thenReturn(0.8415);
        when(cosMock.calculate(x, precision)).thenReturn(0.5403);

        // Вычисляем ожидаемый результат
        double expected = 0.8415 / 0.5403;

        // Вызываем тестируемый метод
        double result = tan.calculate(x, precision);

        // Проверяем результат
        assertEquals(expected, result, 1e-4);

        // Проверяем, что моки были вызваны с правильными параметрами
        verify(sinMock).calculate(x, precision);
        verify(cosMock).calculate(x, precision);
    }

    @Test
    void testTanOfZero() {
        // tan(0) = sin(0) / cos(0) = 0 / 1 = 0
        double x = 0.0;

        when(sinMock.calculate(x, precision)).thenReturn(0.0);
        when(cosMock.calculate(x, precision)).thenReturn(1.0);

        double result = tan.calculate(x, precision);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    void testTanOfPiHalf() {
        // tan(π/2) = sin(π/2) / cos(π/2) = 1 / 0 = NaN
        double x = Math.PI / 2;

        when(sinMock.calculate(x, precision)).thenReturn(1.0);
        when(cosMock.calculate(x, precision)).thenReturn(0.0);

        double result = tan.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testTanOfPi() {
        // tan(π) = sin(π) / cos(π) = 0 / (-1) = 0
        double x = Math.PI;

        when(sinMock.calculate(x, precision)).thenReturn(0.0);
        when(cosMock.calculate(x, precision)).thenReturn(-1.0);

        double result = tan.calculate(x, precision);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    void testTanOfValueNearPiHalf() {
        // tan вблизи π/2 должен быть очень большим по модулю
        double x = Math.PI / 2 - 0.01;

        when(sinMock.calculate(x, precision)).thenReturn(0.9999);
        when(cosMock.calculate(x, precision)).thenReturn(0.01);

        double result = tan.calculate(x, precision);
        assertEquals(99.99, result, 0.1);
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор по умолчанию создает рабочий экземпляр
        Tan defaultTan = new Tan();

        double result = defaultTan.calculate(0, precision);
        assertEquals(0.0, result, 1e-4);

        // При x = π/2 должен быть NaN или Infinity
        assertTrue(Double.isNaN(defaultTan.calculate(Math.PI / 2, precision)) ||
                Double.isInfinite(defaultTan.calculate(Math.PI / 2, precision)));
    }

    @Test
    void testTanWhenCosReturnsZero() {
        double x = Math.PI / 2;

        // Для tan(π/2) sin возвращает 1, а cos возвращает 0
        when(sinMock.calculate(x, precision)).thenReturn(1.0);
        when(cosMock.calculate(x, precision)).thenReturn(0.0);

        double result = tan.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testTanWhenSinReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда sin возвращает NaN
        when(sinMock.calculate(x, precision)).thenReturn(Double.NaN);
        when(cosMock.calculate(x, precision)).thenReturn(1.0);

        double result = tan.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testTanWhenCosReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда cos возвращает NaN
        when(sinMock.calculate(x, precision)).thenReturn(0.0);
        when(cosMock.calculate(x, precision)).thenReturn(Double.NaN);

        double result = tan.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testTanWithCustomPreсision() {
        double x = 1.0;
        double customPrecision = 1e-10;

        // Настраиваем моки для использования пользовательской точности
        when(sinMock.calculate(x, customPrecision)).thenReturn(0.8415);
        when(cosMock.calculate(x, customPrecision)).thenReturn(0.5403);

        double result = tan.calculate(x, customPrecision);

        assertEquals(0.8415 / 0.5403, result, 1e-6);

        // Проверяем, что моки были вызваны с правильной точностью
        verify(sinMock).calculate(x, customPrecision);
        verify(cosMock).calculate(x, customPrecision);
    }
} 