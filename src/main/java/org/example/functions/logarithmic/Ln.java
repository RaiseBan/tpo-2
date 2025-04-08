package org.example.functions.logarithmic;

import org.example.functions.base.BaseFunction;

public class Ln extends BaseFunction {
    public Ln(double epsilon, int maxIterations) {
        super(epsilon, maxIterations);
    }

    public Ln() {
        super();
    }
    @Override
    public double calculate(double x, double precision) {
        // Натуральный логарифм определен только для положительных чисел
        if (!validateArgs(x, precision) || x <= 0) {
            return Double.NaN;
        }

        // Особый случай: ln(1) = 0
        if (Math.abs(x - 1.0) < precision) {
            return 0.0;
        }

        // Используем оптимизацию для больших чисел: ln(x) = ln(x/a) + ln(a)
        if (x > 2.0) {
            int k = 0;
            double x1 = x;

            while (x1 > 2.0) {
                x1 /= Math.E;
                k++;
            }

            return calculate(x1, precision) + k;
        }

        double z = (x - 1) / (x + 1);
        double z2 = z * z;
        double result = 0;
        double term = z;

        for (int i = 1; i <= getMaxIterations() && Math.abs(term) > precision; i += 2) {
            result += term;
            term = term * z2 * i / (i + 2);
        }

        return 2 * result;
    }
}
