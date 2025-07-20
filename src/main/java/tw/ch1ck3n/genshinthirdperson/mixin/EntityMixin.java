package tw.ch1ck3n.genshinthirdperson.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.genshinthirdperson.GenshinThirdPerson;
import tw.ch1ck3n.genshinthirdperson.camera.GenshinCamera;

@Mixin(Entity.class)
public abstract class EntityMixin {

	// CameraBasedMovement

	@Shadow
	private Entity vehicle;

	@Shadow
	public float lastYaw;

	@Shadow
	public float lastPitch;

	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Inject(method = "changeLookDirection", at = @At(value = "HEAD"), cancellable = true)
	public void changeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
		float f = (float)cursorDeltaY * 0.15F;
		float g = (float)cursorDeltaX * 0.15F;

		GenshinThirdPerson instance = GenshinThirdPerson.getInstance();
		GenshinCamera camera = instance.getCamera();
		if (instance.isEnabled() && this.canRotate()) {
			camera.setRotation(camera.getYaw() + g, camera.getPitch() + f);
			this.setPitch(camera.getPitch());
		} else {
			this.setRotation(this.getYaw() + g, this.getPitch() + f);
			camera.setRotation(this.getYaw(), this.getPitch());
		}

		this.setPitch(MathHelper.clamp(this.getPitch(), -90.0F, 90.0F));
		this.lastPitch += f;
		this.lastYaw += g;
		this.lastPitch = MathHelper.clamp(this.lastPitch, -90.0F, 90.0F);
		if (this.vehicle != null) {
			this.vehicle.onPassengerLookAround((Entity) (Object) this);
		}
		ci.cancel();
	}

	@Shadow
	public abstract float getYaw();

	@Shadow
	public abstract float getPitch();

	@Shadow
	public abstract void setPitch(float pitch);

	@Unique
	private boolean canRotate() {
		GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
		return camera.canRotate();
	}
}