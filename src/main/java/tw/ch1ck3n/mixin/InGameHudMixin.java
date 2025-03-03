package tw.ch1ck3n.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tw.ch1ck3n.GenshinThirdPerson;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Final
    @Shadow
    private static Identifier CROSSHAIR_TEXTURE;

    @Inject(method = "renderCrosshair", at = @At("HEAD"))
    private void injectRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        GenshinThirdPerson instance = GenshinThirdPerson.getInstance();
        if (instance.getCamera().isThirdPerson() && instance.getConfig().alwaysShowCrosshair.status) {
            context.drawGuiTexture(
                    CROSSHAIR_TEXTURE,
                    (context.getScaledWindowWidth() - 15) / 2,
                    (context.getScaledWindowHeight() - 15) / 2,
                    15, 15
            );
        }
    }
}