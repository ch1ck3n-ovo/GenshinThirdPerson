package tw.ch1ck3n.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tw.ch1ck3n.GenshinThirdPerson;
import tw.ch1ck3n.camera.GenshinCamera;
import tw.ch1ck3n.util.ColorUtil;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    @Shadow
    protected M model;

    protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V", ordinal = 0),
            index = 4
    )
    private int modifyL(int l) {
        if (!GenshinThirdPerson.getInstance().getConfig().smoothCameraClip.status ||
                !GenshinThirdPerson.getInstance().getConfig().smoothCameraClip.autoCharacterFade) return l;
        GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
        if (this.model instanceof PlayerEntityModel && camera.isThirdPerson())
            return mix(ColorUtil.rgbaToInt(255, 255, 255, ColorUtil.getAlpha(camera.getMaxAllowedCameraDistance())), -1);
        return l;
    }

    @Unique
    private int mix(int first, int second) {
        if (first == -1) {
            return second;
        } else {
            return second == -1 ? first : ColorHelper.Abgr.getAbgr(
                    ColorHelper.Abgr.getAlpha(first) * ColorHelper.Abgr.getAlpha(second) / 255,
                    ColorHelper.Abgr.getBlue(first) * ColorHelper.Abgr.getBlue(second) / 255,
                    ColorHelper.Abgr.getGreen(first) * ColorHelper.Abgr.getGreen(second) / 255,
                    ColorHelper.Abgr.getRed(first) * ColorHelper.Abgr.getRed(second) / 255);
        }
    }
}