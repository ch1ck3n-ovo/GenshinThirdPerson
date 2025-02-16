package tw.ch1ck3n.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import tw.ch1ck3n.GenshinThirdPerson;
import tw.ch1ck3n.util.CubicBezier;

@Config(name = GenshinThirdPerson.MOD_ID)
public class GTPConfig implements ConfigData {

    // TODO: refmap (X)

    @ConfigEntry.Gui.CollapsibleObject
    public AlwaysShowCrosshair alwaysShowCrosshair = new AlwaysShowCrosshair();

    @ConfigEntry.Gui.CollapsibleObject
    public CameraBasedMovement cameraBasedMovement = new CameraBasedMovement();

    @ConfigEntry.Gui.CollapsibleObject
    public DisableThirdPersonFrontView disableThirdPersonFrontView = new DisableThirdPersonFrontView();

    @ConfigEntry.Gui.CollapsibleObject
    public SmoothCameraClip smoothCameraClip = new SmoothCameraClip();

    public static class AlwaysShowCrosshair {

        public boolean status = true;
    }

    public static class CameraBasedMovement {

        public boolean status = true;

        @ConfigEntry.BoundedDiscrete(min = 0L, max = 100L)
        @ConfigEntry.Gui.Tooltip
        public long alignRecoveryDelay = 20L;

        @ConfigEntry.Gui.Tooltip
        public boolean autoCharacterFade = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableWhenElytra = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableWhenRiding= true;
    }

    public static class DisableThirdPersonFrontView {

        public boolean status = true;
    }

    public static class SmoothCameraClip {

        public boolean status = true;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        @ConfigEntry.Gui.Tooltip
        public CubicBezier.TransitionMode transitionMode = CubicBezier.TransitionMode.LINEAR;

        @ConfigEntry.BoundedDiscrete(min = 26L, max = 400L)
        @ConfigEntry.Gui.Tooltip
        public long startDistance = 26L;

        @ConfigEntry.BoundedDiscrete(min = 0L, max = 100L)
        @ConfigEntry.Gui.Tooltip
        public long transitionTime = 20L;

        @ConfigEntry.BoundedDiscrete(min = 0L, max = 100L)
        @ConfigEntry.Gui.Tooltip
        public long rotationSpeed = 30L;

        @ConfigEntry.Gui.Tooltip
        public boolean applyToMobs = true;
    }

}
