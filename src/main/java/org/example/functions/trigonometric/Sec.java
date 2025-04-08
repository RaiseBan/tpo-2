package org.example.functions.trigonometric;

import org.example.functions.base.MathFunction;

public class Sec implements MathFunction {

    private final Cos cos;

    public Sec(Cos cos) {
        this.cos = cos;
    }

    public Sec() {
        this(new Cos());
    }

    @Override
    public double calculate(double x, double precision) {
        double cosValue = cos.calculate(x, precision);

        // Проверка на близость к нулю
        if (Math.abs(cosValue) < precision) {
            return Double.NaN;
        }

        return 1.0 / cosValue;
    }
}
