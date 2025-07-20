package tw.ch1ck3n.genshinthirdperson.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.genshinthirdperson.GenshinThirdPerson;
import tw.ch1ck3n.genshinthirdperson.camera.GenshinCamera;
import tw.ch1ck3n.genshinthirdperson.config.GTPConfig;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {

	// CameraBasedMovement

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
	public void injectBeforeMovement(CallbackInfo ci) {
		GenshinThirdPerson instance = GenshinThirdPerson.getInstance();
		GenshinCamera camera = instance.getCamera();
		GTPConfig config = instance.getConfig();
		if (instance.isEnabled()) {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (this.isPressedOnAny() && this.canRotate()) {
				float prevYaw= player.getYaw();
				float yawDiff = camera.calculateYawDiff(prevYaw, this.playerInput.forward(), this.playerInput.backward(), this.playerInput.left(), this.playerInput.right());
				player.setYaw(prevYaw + (yawDiff) * (config.smoothCameraClip.rotationSpeed / 100.0F) * camera.getTickDelta());

				this.playerInput = new PlayerInput(this.isPressedOnAny(), false, false, false,
						this.settings.jumpKey.isPressed(), this.settings.sneakKey.isPressed(), this.settings.sprintKey.isPressed());
				float f = getMovementMultiplier(this.playerInput.forward(), this.playerInput.backward());
				float g = getMovementMultiplier(this.playerInput.left(), this.playerInput.right());
				this.movementVector = (new Vec2f(g, f)).normalize();
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
		GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
		return camera.canRotate();
	}
}