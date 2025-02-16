package tw.ch1ck3n.mixin;

import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tw.ch1ck3n.GenshinThirdPerson;

@Mixin(Perspective.class)
public class PerspectiveMixin {

	@Final
	@Shadow
	private static Perspective[] VALUES;

	@Inject(method = "next", at = @At(value = "TAIL"), cancellable = true)
	public void injectNext(CallbackInfoReturnable<Perspective> cir) {
		if (GenshinThirdPerson.getInstance().getConfig().disableThirdPersonFrontView.status)
			cir.setReturnValue(VALUES[(((Perspective) (Object) this).ordinal() + 1) % (VALUES.length - 1)]);
	}
}