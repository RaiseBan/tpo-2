package system;

import org.example.functions.logarithmic.Ln;
import org.example.functions.logarithmic.Log;
import org.example.functions.system.SystemFunction;
import org.example.functions.trigonometric.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SystemFunctionMockitoTest {

    private SystemFunction systemFunction;
    private Cos cos;
    private Sec sec;
    private Cot cot;
    private Tan tan;
    private Csc csc;
    private Ln ln;
    private Log log10;
    private Log log2;
    private Log log3;

    @BeforeEach
    void setUp() {
        // Создаем моки для всех зависимостей
        cos = mock(Cos.class);
        sec = mock(Sec.class);
        cot = mock(Cot.class);
        tan = mock(Tan.class);
        csc = mock(Csc.class);
        ln = mock(Ln.class);
        log10 = mock(Log.class);
        log2 = mock(Log.class);
        log3 = mock(Log.class);

        // Создаем тестируемый объект с моками
        systemFunction = new SystemFunction(cos, sec, cot, tan, csc, ln, log10, log2, log3);
    }

    @Test
    void testCalculateNegativeCase() {
        double x = -1.0;
        double precision = 1e-6;

        // Настраиваем поведение моков
        when(cos.calculate(x, precision)).thenReturn(0.5403);
        when(sec.calculate(x, precision)).thenReturn(1.8508);
        when(cot.calculate(x, precision)).thenReturn(-0.6421);
        when(tan.calculate(x, precision)).thenReturn(-1.5574);
        when(csc.calculate(x, precision)).thenReturn(-1.1884);

        // Вычисляем ожидаемый результат на основе формулы и мокированных значений
        double cosX = 0.5403;
        double secX = 1.8508;
        double cotX = -0.6421;
        double tanX = -1.5574;
        double cscX = -1.1884;

        // cos(x)^3
        double cosCubed = Math.pow(cosX, 3);
        // (cos(x)^3) * sec(x)
        double cosCubedSecX = cosCubed * secX;
        // ((cos(x)^3) * sec(x))^2
        double cosCubedSecXSquared = Math.pow(cosCubedSecX, 2);
        // (tan(x) + csc(x))
        double tanPlusCsc = tanX + cscX;
        // cot(x) * (tan(x) + csc(x))
        double cotTimesTanPlusCsc = cotX * tanPlusCsc;
        // (csc(x) * csc(x))
        double cscSquared = cscX * cscX;
        // (cot(x) * (tan(x) + csc(x))) - (csc(x) * csc(x))
        double cotTimesTanPlusCscMinusCscSquared = cotTimesTanPlusCsc - cscSquared;
        // ((((cos(x) ^ 3) * sec(x)) ^ 2) + ((cot(x) * (tan(x) + csc(x))) - (csc(x) * csc(x))))
        double firstPart = cosCubedSecXSquared + cotTimesTanPlusCscMinusCscSquared;
        // (cot(x) ^ 2)
        double cotSquared = Math.pow(cotX, 2);
        // (sec(x) / (cot(x) ^ 2))
        double secDividedByCotSquared = secX / cotSquared;
        // Итоговый результат
        double expected = firstPart - secDividedByCotSquared;

        // Вызываем тестируемый метод
        double result = systemFunction.calculate(x, precision);

        // Проверяем результат
        assertEquals(expected, result, 1e-4);

        // Проверяем, что все моки были вызваны
        verify(cos).calculate(x, precision);
        verify(sec).calculate(x, precision);
        verify(cot).calculate(x, precision);
        verify(tan).calculate(x, precision);
        verify(csc).calculate(x, precision);

        // Проверяем, что логарифмические функции не вызывались
        verify(ln, never()).calculate(anyDouble(), anyDouble());
        verify(log10, never()).calculate(anyDouble(), anyDouble());
        verify(log2, never()).calculate(anyDouble(), anyDouble());
        verify(log3, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    void testCalculatePositiveCase() {
        double x = 2.0;
        double precision = 1e-6;

        // Настраиваем поведение моков
        when(log10.calculate(x, precision)).thenReturn(0.3010);
        when(ln.calculate(x, precision)).thenReturn(0.6931);
        when(log2.calculate(x, precision)).thenReturn(1.0);
        when(log3.calculate(x, precision)).thenReturn(0.6309);

        // Вычисляем ожидаемый результат на основе формулы и мокированных значений
        double log10X = 0.3010;
        double lnX = 0.6931;
        double log2X = 1.0;
        double log3X = 0.6309;

        // (log_10(x) - ln(x))
        double log10MinusLn = log10X - lnX;
        // (log_2(x) / log_3(x))
        double log2DivLog3 = log2X / log3X;
        // (log_10(x) - ln(x)) / (log_2(x) / log_3(x))
        double division = log10MinusLn / log2DivLog3;
        // ((log_10(x) - ln(x)) / (log_2(x) / log_3(x))) ^ 2
        double divisionSquared = Math.pow(division, 2);
        // (((log_10(x) - ln(x)) / (log_2(x) / log_3(x))) ^ 2) * log_10(x)
        double divisionSquaredTimesLog10 = divisionSquared * log10X;
        // (((log_10(x) - ln(x)) / (log_2(x) / log_3(x))) ^ 2) * log_10(x)) / log_10(x)
        double expected = divisionSquaredTimesLog10 / log10X;

        // Вызываем тестируемый метод
        double result = systemFunction.calculate(x, precision);

        // Проверяем результат
        assertEquals(expected, result, 1e-4);

        // Проверяем, что все моки были вызваны
        verify(log10).calculate(x, precision);
        verify(ln).calculate(x, precision);
        verify(log2).calculate(x, precision);
        verify(log3).calculate(x, precision);

        // Проверяем, что тригонометрические функции не вызывались
        verify(cos, never()).calculate(anyDouble(), anyDouble());
        verify(sec, never()).calculate(anyDouble(), anyDouble());
        verify(cot, never()).calculate(anyDouble(), anyDouble());
        verify(tan, never()).calculate(anyDouble(), anyDouble());
        verify(csc, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    void testCalculateZero() {
        double x = 0.0;
        double precision = 1e-6;

        // Настраиваем поведение моков для случая x = 0
        when(cos.calculate(x, precision)).thenReturn(1.0);
        when(sec.calculate(x, precision)).thenReturn(1.0);
        when(cot.calculate(x, precision)).thenReturn(Double.NaN); // cot(0) не определен
        when(tan.calculate(x, precision)).thenReturn(0.0);
        when(csc.calculate(x, precision)).thenReturn(Double.NaN); // csc(0) не определен

        // Ожидаем NaN из-за неопределенных функций в точке 0
        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testNegativeFunctionWithNaN() {
        double x = -Math.PI / 2; // Точка, где cot и tan могут быть не определены
        double precision = 1e-6;

        // Настраиваем поведение моков
        when(cos.calculate(x, precision)).thenReturn(0.0);
        when(sec.calculate(x, precision)).thenReturn(Double.NaN);
        when(cot.calculate(x, precision)).thenReturn(0.0);
        when(tan.calculate(x, precision)).thenReturn(Double.NaN);
        when(csc.calculate(x, precision)).thenReturn(-1.0);

        // Ожидаем NaN из-за NaN в одной из функций
        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testPositiveFunctionWithNaN() {
        double x = 0.0; // Для логарифмов это недопустимый аргумент
        double precision = 1e-6;

        // Настраиваем поведение моков
        when(log10.calculate(x, precision)).thenReturn(Double.NaN);
        when(ln.calculate(x, precision)).thenReturn(Double.NaN);
        when(log2.calculate(x, precision)).thenReturn(Double.NaN);
        when(log3.calculate(x, precision)).thenReturn(Double.NaN);

        // Так как x = 0, будет вызвана функция calculateNegative, но настроим моки и для calculatePositive
        when(cos.calculate(x, precision)).thenReturn(1.0);
        when(sec.calculate(x, precision)).thenReturn(1.0);
        when(cot.calculate(x, precision)).thenReturn(Double.NaN);
        when(tan.calculate(x, precision)).thenReturn(0.0);
        when(csc.calculate(x, precision)).thenReturn(Double.NaN);

        // x = 0 попадает в ветку calculateNegative
        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result));
    }

    @Test
    void testPositiveFunctionDivisionByZero() {
        double x = 10.0;
        double precision = 1e-6;

        // Настраиваем поведение моков - log2 и log3 дают одинаковые значения, что приведет к log2/log3 = 1.0
        when(log10.calculate(x, precision)).thenReturn(1.0);
        when(ln.calculate(x, precision)).thenReturn(2.3026);
        when(log2.calculate(x, precision)).thenReturn(3.3219);
        when(log3.calculate(x, precision)).thenReturn(2.0959);

        // Результат должен быть конечным числом
        double result = systemFunction.calculate(x, precision);
        assertFalse(Double.isNaN(result));
        assertFalse(Double.isInfinite(result));
    }

    @Test
    void testDefaultConstructor() {
        // Проверяем, что конструктор без параметров создает работающий экземпляр
        SystemFunction sys = new SystemFunction();

        // Проверяем для положительного значения
        double resultPos = sys.calculate(2.0, 1e-6);
        assertFalse(Double.isNaN(resultPos));

        // Проверяем для отрицательного значения
        double resultNeg = sys.calculate(-0.5, 1e-6);
        assertFalse(Double.isNaN(resultNeg));
    }

    @Test
    void testPrecisionConstructor() {
        // Проверяем, что конструктор с указанием точности создает работающий экземпляр
        SystemFunction sys = new SystemFunction(1e-8, 200);

        // Проверяем для положительного значения
        double resultPos = sys.calculate(2.0, 1e-6);
        assertFalse(Double.isNaN(resultPos));

        // Проверяем для отрицательного значения
        double resultNeg = sys.calculate(-0.5, 1e-6);
        assertFalse(Double.isNaN(resultNeg));
    }
} 