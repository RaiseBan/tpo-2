package trigonometric;

import org.example.functions.trigonometric.Csc;
import org.example.functions.trigonometric.Sin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CscMockitoTest {

    private Sin sinMock;
    private Csc csc;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        sinMock = mock(Sin.class);
        csc = new Csc(sinMock);
    }

    @Test
    void testCscViaCalculate() {
        // csc(x) = 1 / sin(x)
        double x = Math.PI / 4;

        // Настраиваем мок для sin
        when(sinMock.calculate(x, precision)).thenReturn(0.7071);

        // Вызываем тестируемый метод
        double result = csc.calculate(x, precision);

        // Проверяем результат: 1 / 0.7071 ≈ 1.4142
        assertEquals(1.4142, result, 1e-4);

        // Проверяем, что sin был вызван с правильными параметрами
        verify(sinMock).calculate(x, precision);
    }

    @Test
    void testCscOfZero() {
        // csc(0) = 1 / sin(0) = 1 / 0 = NaN
        double x = 0.0;

        when(sinMock.calculate(x, precision)).thenReturn(0.0);

        double result = csc.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCscOfPiHalf() {
        // csc(π/2) = 1 / sin(π/2) = 1 / 1 = 1
        double x = Math.PI / 2;

        when(sinMock.calculate(x, precision)).thenReturn(1.0);

        double result = csc.calculate(x, precision);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    void testCscOfPi() {
        // csc(π) = 1 / sin(π) = 1 / 0 = NaN
        double x = Math.PI;

        when(sinMock.calculate(x, precision)).thenReturn(0.0);

        double result = csc.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCscOfValueNearZero() {
        // csc вблизи 0 должен быть очень большим по модулю
        double x = 0.01;

        when(sinMock.calculate(x, precision)).thenReturn(0.01);

        double result = csc.calculate(x, precision);
        assertEquals(100.0, result, 0.1);
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор по умолчанию создает рабочий экземпляр
        Csc defaultCsc = new Csc();

        // При x = π/2 косеканс должен быть 1
        double result = defaultCsc.calculate(Math.PI / 2, precision);
        assertEquals(1.0, result, 1e-4);

        // При x = 0 косеканс не определен
        assertTrue(Double.isNaN(defaultCsc.calculate(0, precision)) ||
                Double.isInfinite(defaultCsc.calculate(0, precision)));
    }

    @Test
    void testCscWhenSinReturnsZero() {
        double x = 0.0;

        // sin(0) = 0, что вызывает деление на ноль
        when(sinMock.calculate(x, precision)).thenReturn(0.0);

        double result = csc.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCscWhenSinReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда sin возвращает NaN
        when(sinMock.calculate(x, precision)).thenReturn(Double.NaN);

        double result = csc.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testCscWithCustomPreсision() {
        double x = 1.0;
        double customPrecision = 1e-10;

        // Настраиваем мок для использования пользовательской точности
        when(sinMock.calculate(x, customPrecision)).thenReturn(0.8415);

        double result = csc.calculate(x, customPrecision);

        assertEquals(1.0 / 0.8415, result, 1e-6);

        // Проверяем, что sin был вызван с правильной точностью
        verify(sinMock).calculate(x, customPrecision);
    }


    @ParameterizedTest
    @ValueSource(doubles = {Math.PI / 6, Math.PI / 4, Math.PI / 3, Math.PI / 2, Math.PI})
    void testCscForDifferentAngles(double x) {
        // Получаем синус для угла
        double sinValue = Math.sin(x);

        // Если синус близок к нулю, ожидаем NaN
        if (Math.abs(sinValue) < 1e-10) {
            when(sinMock.calculate(x, precision)).thenReturn(0.0);
            double result = csc.calculate(x, precision);
            assertTrue(Double.isNaN(result));
        } else {
            // Иначе ожидаем корректное значение косеканса
            when(sinMock.calculate(x, precision)).thenReturn(sinValue);
            double result = csc.calculate(x, precision);
            assertEquals(1.0 / sinValue, result, 1e-6);
        }
    }
} 