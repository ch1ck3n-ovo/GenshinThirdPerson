package tw.ch1ck3n.genshinthirdperson.util;

import org.joml.Math;

public class ColorUtil {

    public static int getAlpha(float f) {
        return (int) Math.min(255, Math.max(38, (255.0F / 0.25F) * Math.min(0.38F, f - 1.62F)));
    }

    public static int changeAlpha(int color, int a) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int rgbaToInt(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
