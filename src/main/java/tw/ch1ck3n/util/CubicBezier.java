package tw.ch1ck3n.util;

public class CubicBezier {

    /**
     * 根據給定的 x 值，使用牛頓法（Newton-Raphson Method）反推出貝茲曲線上的 t 參數。
     *
     * @author CH1CK3N
     * @param x       目標 x 值
     * @param x1      控制點 1 的 x 座標
     * @param x2      控制點 2 的 x 座標
     * @param epsilon 誤差容忍度，決定精度
     * @return        對應 x 值的 t 參數（0 ~ 1）
     */
    public static float getTFromX(float x, float x1, float x2, float epsilon) {
        float t = x; // 初始猜測值
        for (int i = 0; i < 10; i++) { // 最多迭代 10 次，提升精度
            float fx = getYFromT(t, 0, x1, x2, 1) - x; // 計算誤差
            if (Math.abs(fx) < epsilon) return t; // 誤差足夠小，直接返回 t

            // 計算導數，避免除以 0
            float dfx = 3 * (1 - t) * (1 - t) * (x1 - 0) +
                    6 * (1 - t) * t * (x2 - x1) +
                    3 * t * t * (1 - x2);

            if (dfx == 0) break; // 避免除以 0
            t -= fx / dfx; // 更新 t 值（牛頓法公式）
        }
        return t; // 返回最接近的 t
    }

    /**
     * 根據 y 值反推 t，使用二分搜尋法（Binary Search）。
     *
     * @param y         目標 y 值
     * @param p0        起點的 x 座標
     * @param p1        控制點 1 的 x 座標，影響曲線的初始彎曲程度
     * @param p2        控制點 2 的 x 座標，影響曲線的結束彎曲程度
     * @param p3        終點的 x 座標
     * @param epsilon   誤差容忍度
     * @return          對應 y 值的 t 參數
     */
    public static float getTFromY(float y, float p0, float p1, float p2, float p3, float epsilon) {
        float t0 = 0.0F, t1 = 1.0F, t = 0.5F; // 初始區間 [0, 1]

        while (t1 - t0 > epsilon) { // 誤差小於 epsilon 時停止
            float Y = getYFromT(t, p0, p1, p2, p3); // 計算當前 t 對應的 y

            if (Y < y) {
                t0 = t; // y 太小，往右找
            } else {
                t1 = t; // y 太大，往左找
            }
            t = (t0 + t1) / 2.0F; // 取中間值繼續搜尋
        }
        return t; // 返回找到的 t
    }

    /**
     * 簡化版本：根據 y 值和模式反推 t。
     *
     * @param y       目標 y 值
     * @param mode    貝茲曲線的模式，包含控制點
     * @param epsilon 誤差容忍度
     * @return        對應 y 值的 t 參數
     */
    public static float getTFromY(float y, TransitionMode mode, float epsilon) {
        return getTFromY(y, 0, mode.y1, mode.y2, 1, epsilon);
    }

    /**
     * 根據 t 值計算貝茲曲線的結果（B(t)）。
     *
     * @param t   貝茲曲線參數（0 ~ 1）
     * @param p0  起點的 x 座標
     * @param p1  控制點 1 的 x 座標，影響曲線的初始彎曲程度
     * @param p2  控制點 2 的 x 座標，影響曲線的結束彎曲程度
     * @param p3  終點的 x 座標
     * @return    B(t) 對應的座標值
     */
    public static float getBFromT(float t, float p0, float p1, float p2, float p3) {
        float u = 1 - t; // 補數
        return (u * u * u) * p0 + (3 * u * u * t) * p1 + (3 * u * t * t) * p2 + (t * t * t) * p3;
    }

    /**
     * 根據 t 計算對應的 x 值。
     *
     * @param t   參數 t
     * @param p0  起點的 x 座標
     * @param p1  控制點 1 的 x 座標，影響曲線的初始彎曲程度
     * @param p2  控制點 2 的 x 座標，影響曲線的結束彎曲程度
     * @param p3  終點的 x 座標
     * @return    對應的 x 值
     */
    public static float getXFromT(float t, float p0, float p1, float p2, float p3) {
        return getBFromT(t, p0, p1, p2, p3);
    }

    /**
     * 根據 t 計算對應的 y 值。
     *
     * @param t   參數 t
     * @param p0  起點的 x 座標
     * @param p1  控制點 1 的 x 座標，影響曲線的初始彎曲程度
     * @param p2  控制點 2 的 x 座標，影響曲線的結束彎曲程度
     * @param p3  終點的 x 座標
     * @return    對應的 y 值
     */
    public static float getYFromT(float t, float p0, float p1, float p2, float p3) {
        return getBFromT(t, p0, p1, p2, p3);
    }

    /**
     * 根據 x 值計算對應的 y 值，適用於貝茲曲線動畫平滑效果。
     *
     * @param mode 貝茲曲線模式，包含控制點
     * @param x    目標 x 值
     * @return     對應的 y 值
     */
    @Deprecated
    public static float getYFromX(TransitionMode mode, float x) {
        float t = getTFromX(x, mode.x1, mode.x2, 0.0001F); // 先求出 t
        return getYFromT(t, 0, mode.y1, mode.y2, 1);       // 再用 t 計算 y
    }

    public enum TransitionMode {
        LINEAR("linear", 0.0F, 0.0F, 1.0F, 1.0F),
        EASE("ease", 0.25F, 0.1F, 0.25F, 1.0F),
        EASE_IN("ease-in", 0.42F, 0.0F, 1.0F, 1.0F),
        EASE_OUT("ease-out", 0.0F, 0.0F, 0.58F, 1.0F),
        EASE_IN_OUT("ease-in-out", 0.42F, 0.0F, 0.58F, 1.0F),
        GENSHIN_IMPACT_1("genshin-impact-1", 0.25F, 1.0F, 0.5F, 1.0F),
        GENSHIN_IMPACT_2("genshin-impact-2", 0.3F, 0.8F, 0.3F, 1.0F);

        public final String key;
        public final float x1, y1, x2, y2;

        TransitionMode(String key, float x1, float y1, float x2, float y2) {
            this.key = key;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        @Override
        public String toString() {
            return "text.autoconfig.genshinthirdperson.option.smoothCameraClip.transitionMode." + this.key;
        }
    }
}
