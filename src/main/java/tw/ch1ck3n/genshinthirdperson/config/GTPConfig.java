package tw.ch1ck3n.genshinthirdperson.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import tw.ch1ck3n.genshinthirdperson.GenshinThirdPerson;
import tw.ch1ck3n.genshinthirdperson.util.CubicBezier;
import tw.ch1ck3n.genshinthirdperson.util.DisableMode;

@Config(name = GenshinThirdPerson.MOD_ID)
public class GTPConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public AlwaysShowCrosshair alwaysShowCrosshair = new AlwaysShowCrosshair();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public AutoCharacterFade autoCharacterFade = new AutoCharacterFade();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public CameraAlignOnClick cameraAlignOnClick = new CameraAlignOnClick();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public CameraBasedMovement cameraBasedMovement = new CameraBasedMovement();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public ThirdPersonFrontView thirdPersonFrontView = new ThirdPersonFrontView();

    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public SmoothCameraClip smoothCameraClip = new SmoothCameraClip();

    public static class AlwaysShowCrosshair {

        public boolean status = true;
    }

    public static class AutoCharacterFade {

        public boolean status = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableArmorWhenFade = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableCapeWhenFade = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableItemWhenFade = true;
    }

    public static class CameraAlignOnClick {

        public boolean status = true;

        @ConfigEntry.BoundedDiscrete(min = 0L, max = 100L)
        @ConfigEntry.Gui.Tooltip
        public long alignRecoveryDelay = 20L;
    }

    public static class CameraBasedMovement {

        public boolean status = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableWhenElytra = true;

        @ConfigEntry.Gui.Tooltip
        public boolean disableWhenRiding= true;
    }

    public static class ThirdPersonFrontView {

        public boolean status = true;

        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        @ConfigEntry.Gui.Tooltip
        public DisableMode disableMode = DisableMode.SKIP;
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
