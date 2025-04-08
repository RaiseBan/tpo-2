package org.example.functions.system;

import org.example.functions.base.MathFunction;
import org.example.functions.logarithmic.Ln;
import org.example.functions.logarithmic.Log;
import org.example.functions.trigonometric.Cos;
import org.example.functions.trigonometric.Cot;
import org.example.functions.trigonometric.Csc;
import org.example.functions.trigonometric.Sec;
import org.example.functions.trigonometric.Sin;
import org.example.functions.trigonometric.Tan;

public class SystemFunction implements MathFunction {
    // Тригонометрические функции для x ≤ 0
    private final Cos cos;
    private final Sec sec;
    private final Cot cot;
    private final Tan tan;
    private final Csc csc;

    // Логарифмические функции для x > 0
    private final Ln ln;
    private final Log log10;
    private final Log log2;
    private final Log log3;

    public SystemFunction(Cos cos, Sec sec, Cot cot, Tan tan, Csc csc,
                          Ln ln, Log log10, Log log2, Log log3) {
        this.cos = cos;
        this.sec = sec;
        this.cot = cot;
        this.tan = tan;
        this.csc = csc;
        this.ln = ln;
        this.log10 = log10;
        this.log2 = log2;
        this.log3 = log3;
    }

    public SystemFunction() {
        Sin sin = new Sin();
        this.cos = new Cos(sin);
        this.sec = new Sec(cos);
        this.tan = new Tan(sin, cos);
        this.cot = new Cot(sin, cos);
        this.csc = new Csc(sin);

        this.ln = new Ln();
        this.log10 = new Log(ln, 10);
        this.log2 = new Log(ln, 2);
        this.log3 = new Log(ln, 3);
    }

    public SystemFunction(double epsilon, int maxIterations) {
        Sin sin = new Sin(epsilon, maxIterations);
        this.cos = new Cos(sin);
        this.sec = new Sec(cos);
        this.tan = new Tan(sin, cos);
        this.cot = new Cot(sin, cos);
        this.csc = new Csc(sin);

        this.ln = new Ln(epsilon, maxIterations);
        this.log10 = new Log(ln, 10);
        this.log2 = new Log(ln, 2);
        this.log3 = new Log(ln, 3);
    }

    @Override
    public double calculate(double x, double precision) {
        if (x <= 0) {
            return calculateNegative(x, precision);
        } else {
            return calculatePositive(x, precision);
        }
    }

    private double calculateNegative(double x, double precision) {
        try {
            // Вычисляем значения всех функций в точке x
//            System.out.println(x);
            double cosX = cos.calculate(x, precision);

            double secX = sec.calculate(x, precision);
            double cotX = cot.calculate(x, precision);
            double tanX = tan.calculate(x, precision);
            double cscX = csc.calculate(x, precision);

//            System.out.println("cosX: " + cosX);
//            System.out.println("secX: " + secX);
//            System.out.println("cotX: " + cotX);
//            System.out.println("tanX: " + tanX);
//            System.out.println("cscX: " + cscX);

            // Проверяем на NaN
            if (Double.isNaN(cosX) || Double.isNaN(secX) || Double.isNaN(cotX) ||
                    Double.isNaN(tanX) || Double.isNaN(cscX)) {
                return Double.NaN;
            }

            // cos(x)^3
            double cosCubed = Math.pow(cosX, 3);

            // (cos(x)^3) * sec(x)
            double cosCubedSecX = cosCubed * secX;

            // ((cos(x)^3) * sec(x))^2
            double cosCubedSecXSquared = Math.pow(cosCubedSecX, 2);

            // (tan(x) + csc(x))
            double tanPlusCsc = tanX + cscX;

            // cot(x) * (tan(x) + csc(x))
            double cotTimesTanPlusCsc = cotX * tanPlusCsc;

            // (csc(x) * csc(x))
            double cscSquared = cscX * cscX;

            // (cot(x) * (tan(x) + csc(x))) - (csc(x) * csc(x))
            double cotTimesTanPlusCscMinusCscSquared = cotTimesTanPlusCsc - cscSquared;

            // ((((cos(x) ^ 3) * sec(x)) ^ 2) + ((cot(x) * (tan(x) + csc(x))) - (csc(x) * csc(x))))
            double firstPart = cosCubedSecXSquared + cotTimesTanPlusCscMinusCscSquared;

            // (cot(x) ^ 2)
            double cotSquared = Math.pow(cotX, 2);

            // (sec(x) / (cot(x) ^ 2))
            double secDividedByCotSquared = secX / cotSquared;

            // Итоговый результат
            return firstPart - secDividedByCotSquared;
        } catch (Exception e) {
            return Double.NaN;
        }
    }
    private double calculatePositive(double x, double precision) {
        try {
            // Вычисляем значения всех функций в точке x
            double log10X = log10.calculate(x, precision);
            double lnX = ln.calculate(x, precision);
            double log2X = log2.calculate(x, precision);
            double log3X = log3.calculate(x, precision);

            // Проверяем на NaN и на деление на ноль
            if (Double.isNaN(log10X) || Double.isNaN(lnX) ||
                    Double.isNaN(log2X) || Double.isNaN(log3X)) {
                return Double.NaN;
            }

            if (Math.abs(log2X) < precision || Math.abs(log3X) < precision) {
                return Double.NaN; // Избегаем деления на ноль
            }

            // (log_10(x) - ln(x))
            double log10MinusLn = log10X - lnX;

            // (log_2(x) / log_3(x))
            double log2DividedByLog3 = log2X / log3X;

            // ((log_10(x) - ln(x)) / (log_2(x) / log_3(x)))
            double divisionResult = log10MinusLn / log2DividedByLog3;

            // (((log_10(x) - ln(x)) / (log_2(x) / log_3(x))) ^ 2)
            double divisionResultSquared = Math.pow(divisionResult, 2);

            // Последнее умножение и деление на log_10(x) сокращаются
            return divisionResultSquared;
        } catch (Exception e) {
            return Double.NaN;
        }
    }
} 