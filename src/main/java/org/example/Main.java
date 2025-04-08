package org.example;

import java.io.IOException;

import org.example.functions.base.MathFunction;
import org.example.functions.system.SystemFunction;
import org.example.util.CsvExporter;

public class Main {

    public static void main(String[] args) {
        // Создаем экземпляр системы функций с заданной точностью
        MathFunction systemFunction = new SystemFunction(1e-6, 100);

        // Создаем экземпляр экспортера
        CsvExporter exporter = new CsvExporter();

        try {
            // Экспортируем результаты для отрицательного диапазона
            exporter.exportToCsv(
                    systemFunction,
                    -Math.PI, // начальное значение
                    -0.1, // конечное значение
                    0.1, // шаг
                    1e-6, // точность
                    "negative_function_results.csv" // имя файла
            );

            // Экспортируем результаты для положительного диапазона
            exporter.exportToCsv(
                    systemFunction,
                    0.1, // начальное значение
                    10.0, // конечное значение
                    0.1, // шаг
                    1e-6, // точность
                    "positive_function_results.csv" // имя файла
            );

            // Экспортируем результаты для всего диапазона
            exporter.exportToCsv(
                    systemFunction,
                    -Math.PI, // начальное значение
                    10.0, // конечное значение
                    0.1, // шаг
                    1e-6, // точность
                    "all_function_results.csv" // имя файла
            );

            System.out.println("Результаты успешно экспортированы в CSV файлы:");
            System.out.println("- negative_function_results.csv (x ≤ 0)");
            System.out.println("- positive_function_results.csv (x > 0)");
            System.out.println("- all_function_results.csv (весь диапазон)");

            // Выводим несколько значений функции для проверки
            printFunctionValue(systemFunction, -Math.PI);
            printFunctionValue(systemFunction, -1.0);
            printFunctionValue(systemFunction, -0.5);
            printFunctionValue(systemFunction, 0.0);
            printFunctionValue(systemFunction, 0.5);
            printFunctionValue(systemFunction, 1.0);
            printFunctionValue(systemFunction, 2.0);

        } catch (IOException e) {
            System.err.println("Ошибка при экспорте данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printFunctionValue(MathFunction function, double x) {
        double result = function.calculate(x, 1e-6);
        System.out.printf("f(%.4f) = %.8f%n", x, result);
    }
}
