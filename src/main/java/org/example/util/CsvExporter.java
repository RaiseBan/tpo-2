package org.example.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.example.functions.base.MathFunction;

public class CsvExporter {

    // По умолчанию запятая, но можно изменить
    private String separator;

    public CsvExporter() {
        this(",");
    }

    public CsvExporter(String separator) {
        this.separator = separator;
    }

    public void exportToCsv(MathFunction function, double start, double end, double step,
                            double precision, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Записываем заголовок
            writer.println("X" + separator + "f(X)");

            // Вычисляем и записываем значения функции
            for (double x = start; x <= end; x += step) {
                double result = function.calculate(x, precision);
                writer.println(x + separator + result);
            }
        }
    }

    public void exportToCsvWithIntermediateResults(MathFunction function, double start, double end,
                                                   double step, double precision, String filename, MathFunction intermediateFunction,
                                                   String intermediateName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Записываем заголовок
            writer.println("X" + separator + intermediateName + separator + "f(X)");

            // Вычисляем и записываем значения функции
            for (double x = start; x <= end; x += step) {
                double intermediateResult = intermediateFunction.calculate(x, precision);
                double result = function.calculate(x, precision);
                writer.println(x + separator + intermediateResult + separator + result);
            }
        }
    }

    // Экспорт результатов модуля в CSV файл
    public void exportModule(MathFunction module, double start, double end, double step,
                             double precision, String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Заголовок файла
            writer.println("X" + separator + "Результат модуля (X)");

            // Вычисляем значения с заданным шагом
            for (double x = start; x <= end; x += step) {
                double result = module.calculate(x, precision);
                writer.println(x + separator + result);
            }
        }
    }

    // Изменить разделитель
    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
