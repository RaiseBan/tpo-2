package org.example.functions.trigonometric;

import org.example.functions.base.MathFunction;

public class Cos implements MathFunction {

    private final Sin sin;

    public Cos(Sin sin) {
        this.sin = sin;
    }

    public Cos() {
        this(new Sin());
    }

    @Override
    public double calculate(double x, double precision) {
        return sin.calculate(x + Math.PI / 2, precision);
    }

}
