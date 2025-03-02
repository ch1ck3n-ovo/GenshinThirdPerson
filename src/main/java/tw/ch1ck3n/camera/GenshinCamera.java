package tw.ch1ck3n.camera;

import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import tw.ch1ck3n.GenshinThirdPerson;
import tw.ch1ck3n.config.GTPConfig;
import tw.ch1ck3n.util.CubicBezier;

@Getter
public class GenshinCamera {

    private final GenshinThirdPerson instance;

    public GenshinCamera(GenshinThirdPerson instance) {
        this.instance = instance;
    }

    private float pitch;
    private float yaw;
    private boolean thirdPerson;
    private long elapsedTimeMillis;
    private long lastCameraUpdateTimeMillis;
    private long lastInteractionTimeMillis;
    private float currentCameraDistance;
    private float maxAllowedCameraDistance;
    private float tickDelta;

    /** CameraMixin.class */
    public void update(boolean thirdPerson, float tickDelta) {
        this.thirdPerson = thirdPerson;
        this.tickDelta = tickDelta;
        if (instance.getConfig().smoothCameraClip.status) {
            if (!this.thirdPerson) this.elapsedTimeMillis = 0;
            else this.elapsedTimeMillis += System.currentTimeMillis() - this.lastCameraUpdateTimeMillis;
            this.lastCameraUpdateTimeMillis = System.currentTimeMillis();
        }
    }

    public float getClipDistance(float f, boolean isPlayer) {
        maxAllowedCameraDistance = f;
        GTPConfig.SmoothCameraClip smoothCameraClip = instance.getConfig().smoothCameraClip;
        if ((!smoothCameraClip.status) || (!smoothCameraClip.applyToMobs && !isPlayer)) return f;

        CubicBezier.TransitionMode mode = smoothCameraClip.transitionMode;  // 獲取動畫模式
        float initialDistance = smoothCameraClip.startDistance / 100.0F;  // 獲取初始鏡頭距離，單位=0.01格
        int duration = (int) smoothCameraClip.transitionTime;  // 獲取動畫時長，單位=tick

        // limit，不同的f會有不同的limit
        // 用y推t，0<=t<=1
        float y1 = (f - initialDistance) / (4.0F - initialDistance);
        y1 = Math.min(1, Math.max(y1, 0));
        float t1 = CubicBezier.getTFromY(y1, mode, 0.0001F);

        long millisLimit = (long) (duration * t1 * 50L);
        elapsedTimeMillis = Math.min(millisLimit + 1, elapsedTimeMillis);

        // Cubic-Bezier
        // 用t推y，0<=y<=1
        float t2 = Math.min(1.0F, elapsedTimeMillis / (duration * 50.0F));
        float y2 = CubicBezier.getYFromT(t2, 0, mode.y1, mode.y2, 1);

        // 確保鏡頭距離不大於f
        currentCameraDistance = Math.min(f, initialDistance + (4 - initialDistance) * y2);
        return currentCameraDistance;
    }

    /** EntityMixin.class */
    public void setRotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = MathHelper.clamp(pitch % 360.0F, -90.0F, 90.0F);
    }

    /** KeyboardInputMixin.class */
    public float calculateNextYaw(boolean forward, boolean back, boolean left, boolean right) {
        float f = -1.0F, g = -1.0F, result = 0.0F;

        if (forward) f = 0.0F;
        else if (back) f = 180.0F;

        if (left) g = -90.0F;
        else if (right) g = 90.0F;

        if (left && back) f = -180.0F;

        if ((forward || back) && (left || right)) result = (f + g) / 2.0F;
        else if (forward || back) result = f;
        else if (left || right) result = g;

        return this.yaw + result;
    }

    public float calculateYawDiff(float prevYaw, boolean forward, boolean back, boolean left, boolean right) {
        float yawDiff = wrapYaw(calculateNextYaw(forward, back, left, right) - prevYaw);
        return (yawDiff < -180.0F) ? yawDiff + 360.0F : yawDiff;
    }

    public float calculateYawDiff(float prevYaw, float nextYaw) {
        float yawDiff = wrapYaw(nextYaw - prevYaw);
        return (yawDiff < -180.0F) ? yawDiff + 360.0F : yawDiff;
    }

    public float wrapYaw(float yaw) {
        return (yaw + 180.0F) % 360.0F - 180.0F;
    }

    /** MouseMixin.class */
    public void onMouseButton(PlayerEntity player) {
        if (player != null) {
            float prevYaw= player.getYaw();
            float yawDiff = this.calculateYawDiff(prevYaw, this.yaw);
            player.setYaw(prevYaw + yawDiff);
            this.lastInteractionTimeMillis = System.currentTimeMillis();
        }
    }
}
