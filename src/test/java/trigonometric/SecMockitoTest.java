package trigonometric;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Sec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecMockitoTest {

    private Cos cosMock;
    private Sec sec;
    private double precision = 1e-6;

    @BeforeEach
    void setUp() {
        cosMock = mock(Cos.class);
        sec = new Sec(cosMock);
    }

    @Test
    void testSecViaCalculate() {
        // sec(x) = 1 / cos(x)
        double x = 0.0;

        // Настраиваем мок для cos
        when(cosMock.calculate(x, precision)).thenReturn(0.5);

        // Вызываем тестируемый метод
        double result = sec.calculate(x, precision);

        // Проверяем результат: 1 / 0.5 = 2.0
        assertEquals(2.0, result, 1e-6);

        // Проверяем, что cos был вызван с правильными параметрами
        verify(cosMock).calculate(x, precision);
    }

    @Test
    void testSecOfZero() {
        // sec(0) = 1 / cos(0) = 1 / 1 = 1
        double x = 0.0;

        when(cosMock.calculate(x, precision)).thenReturn(1.0);

        double result = sec.calculate(x, precision);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    void testSecOfPiHalf() {
        // sec(π/2) = 1 / cos(π/2) = 1 / 0 = бесконечность
        double x = Math.PI / 2;

        when(cosMock.calculate(x, precision)).thenReturn(0.0);

        double result = sec.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testSecOfPi() {
        // sec(π) = 1 / cos(π) = 1 / (-1) = -1
        double x = Math.PI;

        when(cosMock.calculate(x, precision)).thenReturn(-1.0);

        double result = sec.calculate(x, precision);
        assertEquals(-1.0, result, 1e-6);
    }

    @Test
    void testSecOfValueNearPiHalf() {
        // sec вблизи π/2 должен быть очень большим по модулю
        double x = Math.PI / 2 - 0.01;

        when(cosMock.calculate(x, precision)).thenReturn(0.01);

        double result = sec.calculate(x, precision);
        assertEquals(100.0, result, 1e-6);
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор по умолчанию создает рабочий экземпляр
        Sec defaultSec = new Sec();

        double result = defaultSec.calculate(0, precision);
        assertEquals(1.0, result, 1e-4);

        // При x = π/2 должен быть NaN или Infinity
        assertTrue(Double.isNaN(defaultSec.calculate(Math.PI / 2, precision))
                || Double.isInfinite(defaultSec.calculate(Math.PI / 2, precision)));
    }

    @Test
    void testSecWhenCosReturnsZero() {
        double x = Math.PI / 2;

        // cos(π/2) = 0, что вызывает деление на ноль
        when(cosMock.calculate(x, precision)).thenReturn(0.0);

        double result = sec.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testSecWhenCosReturnsNaN() {
        double x = 0.0;

        // Имитируем ситуацию, когда cos возвращает NaN
        when(cosMock.calculate(x, precision)).thenReturn(Double.NaN);

        double result = sec.calculate(x, precision);

        assertTrue(Double.isNaN(result));
    }

    @Test
    void testSecWithCustomPreсision() {
        double x = 1.0;
        double customPrecision = 1e-10;

        // Настраиваем мок для использования пользовательской точности
        when(cosMock.calculate(x, customPrecision)).thenReturn(0.5403);

        double result = sec.calculate(x, customPrecision);

        assertEquals(1.0 / 0.5403, result, 1e-6);

        // Проверяем, что cos был вызван с правильной точностью
        verify(cosMock).calculate(x, customPrecision);
    }
}
