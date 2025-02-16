package tw.ch1ck3n.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.GenshinThirdPerson;

@Mixin(Mouse.class)
public class MouseMixin {

	@Final
	@Shadow
	private MinecraftClient client;

	@Inject(method = "onMouseButton", at = @At("HEAD"))
	private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
		PlayerEntity player = this.client.player;
		if ((this.client.currentScreen == null && this.client.getOverlay() == null) && (0 <= button && button <= 2))
			GenshinThirdPerson.getInstance().getCamera().onMouseButton(player);
	}
}