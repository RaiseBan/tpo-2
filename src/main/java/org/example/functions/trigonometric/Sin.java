package org.example.functions.trigonometric;

import org.example.functions.base.BaseFunction;

public class Sin extends BaseFunction {

    public Sin(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    public Sin() {
        super();
    }

    @Override
    public double calculate(double x, double precision) {
        if (!validateArgs(x, precision)) {
            return Double.NaN;
        }

        // Нормализуем x в диапазон [-π, π] для лучшей сходимости ряда
        double normalizedX = normalizeArg(x);

        double result = 0.0;
        double term = normalizedX;
        int n = 1;
        double xSquared = normalizedX * normalizedX;

        for (int i = 0; i < getMaxIterations() && Math.abs(term) > precision; i++) {
            result += term;

            // Вычисляем следующий член ряда без использования факториала
            n += 2;
            term = -term * xSquared / (n * (n - 1));
        }

        return result;
    }

    private double normalizeArg(double x) {
        return Math.IEEEremainder(x, 2 * Math.PI);
    }
}
