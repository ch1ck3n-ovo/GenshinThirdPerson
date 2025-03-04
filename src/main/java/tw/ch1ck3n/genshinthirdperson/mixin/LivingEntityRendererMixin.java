package tw.ch1ck3n.genshinthirdperson.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tw.ch1ck3n.genshinthirdperson.GenshinThirdPerson;
import tw.ch1ck3n.genshinthirdperson.camera.GenshinCamera;
import tw.ch1ck3n.genshinthirdperson.util.ColorUtil;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {

    @Shadow
    protected M model;

    protected LivingEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", ordinal = 0),
            index = 7
    )
    private float modifyAlpha(float par5) {
        if (!GenshinThirdPerson.getInstance().getConfig().smoothCameraClip.status ||
                !GenshinThirdPerson.getInstance().getConfig().smoothCameraClip.autoCharacterFade) return par5;
        GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
        if (this.model instanceof PlayerEntityModel && camera.isThirdPerson())
            return ColorUtil.getAlpha(camera.getMaxAllowedCameraDistance()) / 255.0F;
        return par5;
    }
}