/*
 * Created mika <sun00743@gmail.com>
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package mika.com.android.ac.util;

public class MathUtils {

    private MathUtils() {}

    public static int clamp(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    public static float clamp(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    public static int lerp(int start, int end, float fraction) {
        return (int) (start + (end - start) * fraction);
    }

    public static float lerp(float start, float end, float fraction) {
        return start + (end - start) * fraction;
    }

    public static float unlerp(int start, int end, int value) {
        int domainSize = end - start;
        if (domainSize == 0) {
            throw new IllegalArgumentException("Can't reverse interpolate with domain size of 0");
        }
        return (float) (value - start) / domainSize;
    }

    public static float unlerp(float start, float end, float value) {
        float domainSize = end - start;
        if (domainSize == 0) {
            throw new IllegalArgumentException("Can't reverse interpolate with domain size of 0");
        }
        return (value - start) / domainSize;
    }
}
