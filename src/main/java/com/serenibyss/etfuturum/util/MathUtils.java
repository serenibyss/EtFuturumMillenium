package com.serenibyss.etfuturum.util;

public class MathUtils {

    public static float lerp(float f1, float f2, float f3) {
        return f2 + f1 * (f3 - f2);
    }

    public static double lerp(double d1, double d2, double d3) {
        return d2 + d1 * (d3 - d2);
    }
}
