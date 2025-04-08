package trigonometric;

import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Cot;
import org.example.functions.trigonometric.Sin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CotMockitoTest {

    private Sin sinMock;
    private Cos cosMock;
    private Cot cot;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        sinMock = mock(Sin.class);
        cosMock = mock(Cos.class);
        cot = new Cot(sinMock, cosMock);
    }

    @Test
    void testCotViaCalculate() {
        // cot(x) = cos(x) / sin(x)
        double x = 1.0;

        // Настраиваем моки
        when(sinMock.calculate(x, precision)).thenReturn(0.8415);
        when(cosMock.calculate(x, precision)).thenReturn(0.5403);

        // Вычисляем ожидаемый результат
        double expected = 0.5403 / 0.8415;

        // Вызываем тестируемый метод
        double result = cot.calculate(x, precision);

        // Проверяем результат
        assertEquals(expected, result, 1e-4);

        // Проверяем, что моки были вызваны с правильными параметрами
        verify(sinMock).calculate(x, precision);
        verify(cosMock).calculate(x, precision);
    }

    @Test
    void testCotOfZero() {
        // cot(0) = cos(0) / sin(0) = 1 / 0 = NaN
        double x = 0.0;

        when(sinMock.calculate(x, precision)).thenReturn(0.0);
        when(cosMock.calculate(x, precision)).thenReturn(1.0);

        double result = cot.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCotOfPiHalf() {
        // cot(π/2) = cos(π/2) / sin(π/2) = 0 / 1 = 0
        double x = Math.PI / 2;

        when(sinMock.calculate(x, precision)).thenReturn(1.0);
        when(cosMock.calculate(x, precision)).thenReturn(0.0);

        double result = cot.calculate(x, precision);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    void testCotOfPi() {
        // cot(π) = cos(π) / sin(π) = -1 / 0 = NaN
        double x = Math.PI;

        when(sinMock.calculate(x, precision)).thenReturn(0.0);
        when(cosMock.calculate(x, precision)).thenReturn(-1.0);

        double result = cot.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCotOfValueNearZero() {
        // cot вблизи 0 должен быть очень большим по модулю
        double x = 0.01;

        when(sinMock.calculate(x, precision)).thenReturn(0.01);
        when(cosMock.calculate(x, precision)).thenReturn(0.9999);

        double result = cot.calculate(x, precision);
        assertEquals(99.99, result, 0.1);
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор по умолчанию создает рабочий экземпляр
        Cot defaultCot = new Cot();

        // При x = π/2 котангенс должен быть 0
        double result = defaultCot.calculate(Math.PI / 2, precision);
        assertEquals(0.0, result, 1e-4);

        // При x = 0 котангенс не определен
        assertTrue(Double.isNaN(defaultCot.calculate(0, precision)) ||
                Double.isInfinite(defaultCot.calculate(0, precision)));
    }

    @Test
    void testCotWhenSinReturnsZero() {
        double x = 0.0;

        // sin(0) = 0, что вызывает деление на ноль
        when(sinMock.calculate(x, precision)).thenReturn(0.0);
        when(cosMock.calculate(x, precision)).thenReturn(1.0);

        double result = cot.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCotWhenSinReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда sin возвращает NaN
        when(sinMock.calculate(x, precision)).thenReturn(Double.NaN);
        when(cosMock.calculate(x, precision)).thenReturn(1.0);

        double result = cot.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCotWhenCosReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда cos возвращает NaN
        when(sinMock.calculate(x, precision)).thenReturn(0.1);
        when(cosMock.calculate(x, precision)).thenReturn(Double.NaN);

        double result = cot.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCotWithCustomPreсision() {
        double x = 1.0;
        double customPrecision = 1e-10;

        // Настраиваем моки для использования пользовательской точности
        when(sinMock.calculate(x, customPrecision)).thenReturn(0.8415);
        when(cosMock.calculate(x, customPrecision)).thenReturn(0.5403);

        double result = cot.calculate(x, customPrecision);

        assertEquals(0.5403 / 0.8415, result, 1e-6);

        // Проверяем, что моки были вызваны с правильной точностью
        verify(sinMock).calculate(x, customPrecision);
        verify(cosMock).calculate(x, customPrecision);
    }

} 