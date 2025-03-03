package tw.ch1ck3n.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.GenshinThirdPerson;
import tw.ch1ck3n.config.GTPConfig;
import tw.ch1ck3n.camera.GenshinCamera;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

	@Final
	@Shadow
	private GameOptions settings;

    @Shadow
	private static float getMovementMultiplier(boolean positive, boolean negative) {
		if (positive == negative) {
			return 0.0F;
		} else {
			return positive ? 1.0F : -1.0F;
		}
    }

	@Inject(method = "tick", at = @At(value = "TAIL"))
	public void injectBeforeMovement(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
		GenshinThirdPerson instance = GenshinThirdPerson.getInstance();
		GenshinCamera camera = instance.getCamera();
		GTPConfig config = instance.getConfig();
		if (config.cameraBasedMovement.status && camera.isThirdPerson()) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null && this.isPressedOnAny() && this.canRotate()) {
				float prevYaw= player.getYaw();
				float yawDiff = camera.calculateYawDiff(prevYaw, this.settings.forwardKey.isPressed(), this.settings.backKey.isPressed(), this.settings.leftKey.isPressed(), this.settings.rightKey.isPressed());
				player.setYaw(prevYaw + (yawDiff) * (config.smoothCameraClip.rotationSpeed / 100.0F) * camera.getTickDelta());

				this.movementForward = getMovementMultiplier(this.isPressedOnAny(), false);
				this.movementSideways = getMovementMultiplier(false, false);
				if (slowDown) {
					this.movementSideways *= slowDownFactor;
					this.movementForward *= slowDownFactor;
				}
			}
		}
	}

	@Unique
	private boolean isPressedOnAny() {
		return this.settings.forwardKey.isPressed() || this.settings.backKey.isPressed() ||
				this.settings.leftKey.isPressed() || this.settings.rightKey.isPressed();
	}

	@Unique
	private boolean canRotate() {
		PlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return false;
		GenshinThirdPerson instance = GenshinThirdPerson.getInstance();
		GTPConfig.CameraBasedMovement cameraBasedMovement = instance.getConfig().cameraBasedMovement;
		GenshinCamera camera = instance.getCamera();
		return (System.currentTimeMillis() - camera.getLastInteractionTimeMillis()) > (cameraBasedMovement.alignRecoveryDelay * 50L) &&
				(cameraBasedMovement.disableWhenElytra == !player.isFallFlying()) && (cameraBasedMovement.disableWhenRiding == !player.hasVehicle());
	}
}