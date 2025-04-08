package org.example.functions.trigonometric;

import org.example.functions.base.MathFunction;

public class Tan implements MathFunction {

    private final Sin sin;
    private final Cos cos;

    public Tan(Sin sin, Cos cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public Tan() {
        this(new Sin(), new Cos());
    }

    @Override
    public double calculate(double x, double precision) {
        double sinValue = sin.calculate(x, precision);
        double cosValue = cos.calculate(x, precision);

        // Проверка на близость косинуса к нулю
        if (Math.abs(cosValue) < precision) {
            return Double.NaN;
        }

        return sinValue / cosValue;
    }
}
