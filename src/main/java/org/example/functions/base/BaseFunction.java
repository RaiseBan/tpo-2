package org.example.functions.base;

import java.util.Objects;

public abstract class BaseFunction implements MathFunction {
    private final double epsilon;
    private final int maxIterations;

    public BaseFunction(double epsilon, int maxIterations) {
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
    }

    public BaseFunction() {
        this(1e-6, 100);
    }

    public double getEpsilon() {
        return epsilon;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    @Override
    public abstract double calculate(double x, double precision);

    protected void checkValidity(double x, double precision) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("Аргумент не может быть NaN или бесконечностью");
        }

        if (Double.isNaN(precision) || Double.isInfinite(precision) || precision <= 0 || precision >= 1) {
            throw new IllegalArgumentException("Точность должна быть больше 0 и меньше 1");
        }
    }

    protected boolean validateArgs(double x, double precision) {
        try {
            checkValidity(x, precision);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 