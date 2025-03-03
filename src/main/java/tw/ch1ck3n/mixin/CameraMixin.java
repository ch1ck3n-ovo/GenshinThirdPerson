package tw.ch1ck3n.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tw.ch1ck3n.GenshinThirdPerson;
import tw.ch1ck3n.camera.GenshinCamera;

@Mixin(Camera.class)
public class CameraMixin {

	@Shadow
	private Entity focusedEntity;

	@Inject(method = "update", at = @At("HEAD"))
	private void injectUpdate(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
		GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
		camera.update(thirdPerson, tickDelta);
	}

	@ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0), index = 0)
	private float modifyYaw(float yaw)  {
		GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
		if (GenshinThirdPerson.getInstance().getConfig().cameraBasedMovement.status && camera.isThirdPerson()) return camera.getYaw();
		return yaw;
	}

	@ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0), index = 1)
	private float modifyPitch(float pitch) {
		GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
		if (GenshinThirdPerson.getInstance().getConfig().cameraBasedMovement.status && camera.isThirdPerson()) return camera.getPitch();
		return pitch;
	}

	@Inject(method = "clipToSpace", at = @At("RETURN"), cancellable = true)
	private void injectClipToSpace(float f, CallbackInfoReturnable<Float> cir) {
		GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
		cir.setReturnValue(camera.getClipDistance(f, focusedEntity instanceof ClientPlayerEntity));
	}
}