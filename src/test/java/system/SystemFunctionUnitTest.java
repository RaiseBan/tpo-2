package system;

import org.example.functions.logarithmic.Ln;
import org.example.functions.logarithmic.Log;
import org.example.functions.system.SystemFunction;
import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Cot;
import org.example.functions.trigonometric.Csc;
import org.example.functions.trigonometric.Sec;
import org.example.functions.trigonometric.Tan;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SystemFunctionUnitTest {

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
    private final double precision = 1e-6;

    @BeforeEach
    void setUp() {
        cos = mock(Cos.class);
        sec = mock(Sec.class);
        cot = mock(Cot.class);
        tan = mock(Tan.class);
        csc = mock(Csc.class);
        ln = mock(Ln.class);
        log10 = mock(Log.class);
        log2 = mock(Log.class);
        log3 = mock(Log.class);

        systemFunction = new SystemFunction(cos, sec, cot, tan, csc, ln, log10, log2, log3);
    }

    @Test
    void testNegativeBranch() {
        // Тест для x <= 0 (тригонометрическая ветвь)
        double x = -1.0;

        // Настраиваем моки для тестового значения
        when(cos.calculate(x, precision)).thenReturn(0.5403);
        when(sec.calculate(x, precision)).thenReturn(1.8508);
        when(cot.calculate(x, precision)).thenReturn(-0.6421);
        when(tan.calculate(x, precision)).thenReturn(-1.5574);
        when(csc.calculate(x, precision)).thenReturn(-1.1884);

        // Вычисляем ожидаемый результат вручную
        double cosX = 0.5403;
        double secX = 1.8508;
        double cotX = -0.6421;
        double tanX = -1.5574;
        double cscX = -1.1884;

        double cosCubed = Math.pow(cosX, 3);
        double cosCubedSecX = cosCubed * secX;
        double cosCubedSecXSquared = Math.pow(cosCubedSecX, 2);
        double tanPlusCsc = tanX + cscX;
        double cotTimesTanPlusCsc = cotX * tanPlusCsc;
        double cscSquared = cscX * cscX;
        double cotTimesTanPlusCscMinusCscSquared = cotTimesTanPlusCsc - cscSquared;
        double firstPart = cosCubedSecXSquared + cotTimesTanPlusCscMinusCscSquared;
        double cotSquared = Math.pow(cotX, 2);
        double secDividedByCotSquared = secX / cotSquared;
        double expected = firstPart - secDividedByCotSquared;

        // Вызываем метод и проверяем результат
        double result = systemFunction.calculate(x, precision);
        assertEquals(expected, result, 1e-4);

        // Проверяем, что все тригонометрические функции были вызваны
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
    void testPositiveBranch() {
        // Тест для x > 0 (логарифмическая ветвь)
        double x = 2.0;

        // Настраиваем моки для тестового значения
        when(log10.calculate(x, precision)).thenReturn(0.3010);
        when(ln.calculate(x, precision)).thenReturn(0.6931);
        when(log2.calculate(x, precision)).thenReturn(1.0);
        when(log3.calculate(x, precision)).thenReturn(0.6309);

        // Вычисляем ожидаемый результат вручную
        double log10X = 0.3010;
        double lnX = 0.6931;
        double log2X = 1.0;
        double log3X = 0.6309;

        double log10MinusLn = log10X - lnX;
        double log2DivLog3 = log2X / log3X;
        double division = log10MinusLn / log2DivLog3;
        double divisionSquared = Math.pow(division, 2);
        // Последний множитель и деление на log10X сокращаются
        double expected = divisionSquared;

        // Вызываем метод и проверяем результат
        double result = systemFunction.calculate(x, precision);
        assertEquals(expected, result, 1e-4);

        // Проверяем, что все логарифмические функции были вызваны
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
    void testNegativeBranchWithNaN() {
        // Тест для случая, когда одна из тригонометрических функций возвращает NaN
        double x = -Math.PI / 2; // точка, где cot и tan могут быть не определены

        when(cos.calculate(x, precision)).thenReturn(0.0);
        when(sec.calculate(x, precision)).thenReturn(Double.NaN); // sec(π/2) не определен
        when(cot.calculate(x, precision)).thenReturn(Double.NaN); // cot(π/2) не определен
        when(tan.calculate(x, precision)).thenReturn(Double.NaN); // tan(π/2) не определен
        when(csc.calculate(x, precision)).thenReturn(-1.0);

        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result), "Результат должен быть NaN если любая функция возвращает NaN");

        verify(cos).calculate(x, precision);
        verify(sec).calculate(x, precision);
    }

    @Test
    void testPositiveBranchWithNaN() {
        // Тест для случая, когда одна из логарифмических функций возвращает NaN
        double x = 1.0; // для log(1) / log(1) возникает неопределенность 0/0

        when(log10.calculate(x, precision)).thenReturn(0.0);
        when(ln.calculate(x, precision)).thenReturn(0.0);
        when(log2.calculate(x, precision)).thenReturn(0.0);
        when(log3.calculate(x, precision)).thenReturn(0.0);

        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result), "Результат должен быть NaN если возникает деление 0/0");

        verify(log10).calculate(x, precision);
        verify(ln).calculate(x, precision);
        verify(log2).calculate(x, precision);
        verify(log3).calculate(x, precision);
    }

    @Test
    void testZeroInput() {
        // Тест для x = 0, где некоторые тригонометрические функции не определены
        double x = 0.0;

        when(cos.calculate(x, precision)).thenReturn(1.0);
        when(sec.calculate(x, precision)).thenReturn(1.0);
        when(cot.calculate(x, precision)).thenReturn(Double.NaN); // cot(0) не определен
        when(tan.calculate(x, precision)).thenReturn(0.0);
        when(csc.calculate(x, precision)).thenReturn(Double.NaN); // csc(0) не определен

        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result), "Результат должен быть NaN при x = 0");

        verify(cos).calculate(x, precision);
        verify(cot).calculate(x, precision);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.01, 0.001})
    void testPositiveCloseToZero(double x) {
        // Тест для значений близких к нулю, но положительных
        // При x → 0+, логарифмы стремятся к -∞

        when(log10.calculate(x, precision)).thenReturn(-1.0);
        when(ln.calculate(x, precision)).thenReturn(-2.3);
        when(log2.calculate(x, precision)).thenReturn(-3.32);
        when(log3.calculate(x, precision)).thenReturn(-2.1);

        double result = systemFunction.calculate(x, precision);

        // Расчет ожидаемого результата
        double log10X = -1.0;
        double lnX = -2.3;
        double log2X = -3.32;
        double log3X = -2.1;

        double log10MinusLn = log10X - lnX;
        double log2DivLog3 = log2X / log3X;
        double division = log10MinusLn / log2DivLog3;
        double expected = Math.pow(division, 2);

        assertEquals(expected, result, 1e-4);
    }

    @Test
    void testPositiveBranchDivisionByZero() {
        // Тест для случая, когда log2X или log3X близки к нулю
        double x = 1.0;

        when(log10.calculate(x, precision)).thenReturn(0.0);
        when(ln.calculate(x, precision)).thenReturn(0.0);
        when(log2.calculate(x, precision)).thenReturn(0.0);
        when(log3.calculate(x, precision)).thenReturn(0.0);

        double result = systemFunction.calculate(x, precision);
        assertTrue(Double.isNaN(result), "Результат должен быть NaN при делении на ноль");
    }

    @Test
    void testNegativeBranchZeroDivision() {
        // Тест для случая, когда cotX = 0 (знаменатель становится нулем)
        double x = -Math.PI / 4;

        when(cos.calculate(x, precision)).thenReturn(0.7071);
        when(sec.calculate(x, precision)).thenReturn(1.4142);
        when(cot.calculate(x, precision)).thenReturn(0.0); // Предположим cot = 0
        when(tan.calculate(x, precision)).thenReturn(-1.0);
        when(csc.calculate(x, precision)).thenReturn(-1.4142);

        double result = systemFunction.calculate(x, precision);
        // Проверяем, что при cotX = 0 результат становится бесконечным
        assertTrue(Double.isInfinite(result) || Double.isNaN(result),
                "Результат должен быть Infinite или NaN при делении на ноль");
    }
}
