package tw.ch1ck3n.genshinthirdperson.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import tw.ch1ck3n.genshinthirdperson.GenshinThirdPerson;
import tw.ch1ck3n.genshinthirdperson.camera.GenshinCamera;
import tw.ch1ck3n.genshinthirdperson.util.ColorUtil;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> implements FeatureRendererContext<S, M> {

    // AutoCharacterFade

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
        if (!GenshinThirdPerson.getInstance().getConfig().autoCharacterFade.status) return l;
        GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
        if (this.model instanceof PlayerEntityModel && camera.isThirdPerson())
            return ColorHelper.mix(ColorUtil.rgbaToInt(255, 255, 255, ColorUtil.getAlpha(camera.getMaxAllowedCameraDistance())), -1);
        return l;
    }

    @Redirect(
            method = "render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;")
    )
    private Iterator<FeatureRenderer<S, M>> redirectIterator(List<FeatureRenderer<S, M>> features) {
        if (!GenshinThirdPerson.getInstance().getConfig().autoCharacterFade.status) return features.iterator();
        MinecraftClient client = MinecraftClient.getInstance();
        GenshinCamera camera = GenshinThirdPerson.getInstance().getCamera();
        if ((client.currentScreen == null && client.getOverlay() == null) &&
                this.model instanceof PlayerEntityModel && camera.isThirdPerson() &&
                ColorUtil.getAlpha(camera.getMaxAllowedCameraDistance()) < 192) {
            if (GenshinThirdPerson.getInstance().getConfig().autoCharacterFade.disableArmorWhenFade)
                features = features.stream().filter(featureRenderer -> !(featureRenderer instanceof ArmorFeatureRenderer)).toList();
            if (GenshinThirdPerson.getInstance().getConfig().autoCharacterFade.disableCapeWhenFade)
                features = features.stream().filter(featureRenderer -> !(featureRenderer instanceof CapeFeatureRenderer)).toList();
            if (GenshinThirdPerson.getInstance().getConfig().autoCharacterFade.disableItemWhenFade)
                features = features.stream().filter(featureRenderer -> !(featureRenderer instanceof PlayerHeldItemFeatureRenderer)).toList();
        }
        return features.iterator();  // 返回過濾後的迭代器
    }
}