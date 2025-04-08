package system;

import org.example.functions.system.SystemFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SystemFunctionPointTest {

    private SystemFunction systemFunction;
    private final double PRECISION = 1e-10; // Точность для вычислений
    private final double DELTA = 1e-4;     // Допустимая погрешность для сравнения

    @BeforeEach
    void setUp() {
        // Создаем экземпляр SystemFunction с высокой точностью вычислений
        // и большим количеством итераций для повышения точности
        systemFunction = new SystemFunction(1e-6, 1000);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0.05:1.14327",
            "0.2:0.32998",
            "0.5:0.06121",

            "2:0.0612",
            "2.7:0.1256",
            "3.5:0.1999",

            "-2:10.7901",
            "-2.1:4.8364",
            "-2.45:-2.1101",

            "-4.2:5.5425",
            "-4:0.3462",
            "-3.7:-4.6057",

            "-0.5:0.7204",
            "-0.8:-0.8754",
            "-1:-4.0531",

            "-5.78:0.7101",
            "-5.4:-1.7850",
            "-5.2:-7.2219",

    }, delimiter = ':')
    public void testSecondIntervalNonPositiveX(double x, double yExpected) {
        double yCalculated = systemFunction.calculate(x, PRECISION);

        Assertions.assertEquals(yExpected, yCalculated, DELTA);
    }
}
