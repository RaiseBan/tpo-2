package org.example.functions.logarithmic;

import org.example.functions.base.MathFunction;

public class Log implements MathFunction {

    private final Ln ln;
    private final double base;
    private final double lnBase;

    public Log(Ln ln, double base) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть положительным и не равным 1");
        }
        this.ln = ln;
        this.base = base;
        this.lnBase = ln.calculate(base, ln.getEpsilon());
    }

    public Log(double base) {
        this(new Ln(), base);
    }

    @Override
    public double calculate(double x, double precision) {
        // Логарифм определен только для положительных чисел
        if (x <= 0) {
            return Double.NaN;
        }

        double lnX = ln.calculate(x, precision);

        // Проверка результата натурального логарифма
        if (Double.isNaN(lnX)) {
            return Double.NaN;
        }

        return lnX / lnBase;
    }

    public double getBase() {
        return base;
    }
}
