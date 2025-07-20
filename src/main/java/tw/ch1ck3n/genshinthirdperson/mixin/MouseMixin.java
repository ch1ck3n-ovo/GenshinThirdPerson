package tw.ch1ck3n.genshinthirdperson.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.genshinthirdperson.GenshinThirdPerson;

@Mixin(Mouse.class)
public class MouseMixin {

	// CameraAlignWhileClick

	@Final
	@Shadow
	private MinecraftClient client;

	@Shadow
	private boolean leftButtonClicked;

	@Shadow
	private boolean rightButtonClicked;

	@Shadow
	private boolean middleButtonClicked;

	@Inject(method = "tick", at = @At("HEAD"))
	private void injectTick(CallbackInfo ci) {
		GenshinThirdPerson instance = GenshinThirdPerson.getInstance();
		PlayerEntity player = this.client.player;
		if (player != null && (this.client.currentScreen == null && this.client.getOverlay() == null) &&
				(this.leftButtonClicked || this.rightButtonClicked || this.middleButtonClicked) &&
				instance.getConfig().cameraAlignWhileClick.status && instance.isEnabled()) {
			GenshinThirdPerson.getInstance().getCamera().onMouseButton(player);
		}
	}
}