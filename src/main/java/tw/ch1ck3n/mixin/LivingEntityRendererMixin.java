package tw.ch1ck3n.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tw.ch1ck3n.GenshinThirdPerson;
import tw.ch1ck3n.camera.GenshinCamera;
import tw.ch1ck3n.util.ColorUtil;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements FeatureRendererContext<S, M> {

    @Shadow
    protected M model;

    protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V", ordinal = 0),
            index = 4
    )
    private int modifyL(int l) {
        if (!GenshinThirdPerson.getInstance().getConfig().smoothCameraClip.status ||
                !GenshinThirdPerson.getInstance().getConfig().smoothCameraClip.autoCharacterFade) return l;
        GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
        if (this.model instanceof PlayerEntityModel && camera.isThirdPerson())
            return ColorHelper.mix(ColorUtil.rgbaToInt(255, 255, 255, ColorUtil.getAlpha(camera.getMaxAllowedCameraDistance())), -1);
        return l;
    }
}