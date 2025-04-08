package org.example.functions.trigonometric;

import org.example.functions.base.MathFunction;

public class Cot implements MathFunction {

    private final Sin sin;
    private final Cos cos;

    public Cot(Sin sin, Cos cos) {
        this.sin = sin;
        this.cos = cos;
    }

    public Cot() {
        this(new Sin(), new Cos());
    }

    @Override
    public double calculate(double x, double precision) {
        double sinValue = sin.calculate(x, precision);
        double cosValue = cos.calculate(x, precision);

        // Проверка на близость синуса к нулю
        if (Math.abs(sinValue) < precision) {
            return Double.NaN;
        }

        return cosValue / sinValue;
    }
}
