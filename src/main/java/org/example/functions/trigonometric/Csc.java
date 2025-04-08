package org.example.functions.trigonometric;

import org.example.functions.base.MathFunction;

public class Csc implements MathFunction {

    private final Sin sin;

    public Csc(Sin sin) {
        this.sin = sin;
    }

    public Csc() {
        this(new Sin());
    }

    @Override
    public double calculate(double x, double precision) {
        double sinValue = sin.calculate(x, precision);

        // Проверка на близость к нулю
        if (Math.abs(sinValue) < precision) {
            return Double.NaN;
        }

        return 1.0 / sinValue;
    }
}
