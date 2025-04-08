package trigonometric;

import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Sin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CosMockitoTest {

    private Sin sinMock;
    private Cos cos;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        sinMock = mock(Sin.class);
        cos = new Cos(sinMock);
    }

    @Test
    void testCosViaCalculate() {
        // cos(x) = sin(x + π/2)
        double x = 0.0;
        double shiftedX = x + Math.PI / 2;

        // Настраиваем мок для sin
        when(sinMock.calculate(shiftedX, precision)).thenReturn(0.0);

        // Вызываем тестируемый метод
        double result = cos.calculate(x, precision);

        // Проверяем результат
        assertEquals(0.0, result, 1e-6);

        // Проверяем, что sin был вызван с правильными параметрами
        verify(sinMock).calculate(shiftedX, precision);
    }

    @Test
    void testCosOfZero() {
        // cos(0) = 1
        double x = 0.0;

        // sin(π/2) = 1
        when(sinMock.calculate(x + Math.PI / 2, precision)).thenReturn(1.0);

        double result = cos.calculate(x, precision);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    void testCosOfPiHalf() {
        // cos(π/2) = 0
        double x = Math.PI / 2;

        // sin(π) = 0
        when(sinMock.calculate(x + Math.PI / 2, precision)).thenReturn(0.0);

        double result = cos.calculate(x, precision);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    void testCosOfPi() {
        // cos(π) = -1
        double x = Math.PI;

        // sin(3π/2) = -1
        when(sinMock.calculate(x + Math.PI / 2, precision)).thenReturn(-1.0);

        double result = cos.calculate(x, precision);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    void testCosOfMinusPiHalf() {
        // cos(-π/2) = 0
        double x = -Math.PI / 2;

        // sin(0) = 0
        when(sinMock.calculate(x + Math.PI / 2, precision)).thenReturn(0.0);

        double result = cos.calculate(x, precision);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор по умолчанию создает рабочий экземпляр
        Cos defaultCos = new Cos();

        double result = defaultCos.calculate(0, precision);
        assertEquals(1.0, result, 1e-4);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-10.0, -1.0, 0.0, 1.0, 10.0})
    void testCosInRange(double x) {
        // cos(x) всегда должен возвращать значение в диапазоне [-1, 1]
        when(sinMock.calculate(x + Math.PI / 2, precision)).thenReturn(Math.sin(x + Math.PI / 2));

        double result = cos.calculate(x, precision);

        assertTrue(result >= -1.0 && result <= 1.0);
    }

    @Test
    void testCosWhenSinReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда sin возвращает NaN
        when(sinMock.calculate(x + Math.PI / 2, precision)).thenReturn(Double.NaN);

        double result = cos.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCosWithCustomPreсision() {
        double x = 1.0;
        double customPrecision = 1e-10;

        // Настраиваем мок для использования пользовательской точности
        when(sinMock.calculate(x + Math.PI / 2, customPrecision)).thenReturn(Math.sin(x + Math.PI / 2));

        double result = cos.calculate(x, customPrecision);

        assertEquals(Math.cos(x), result, customPrecision);

        // Проверяем, что sin был вызван с правильной точностью
        verify(sinMock).calculate(x + Math.PI / 2, customPrecision);
    }
} 