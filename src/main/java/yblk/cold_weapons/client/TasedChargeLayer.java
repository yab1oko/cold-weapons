package yblk.cold_weapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import yblk.cold_weapons.effect.TasedEffect;

import java.util.HashSet;
import java.util.Set;

/**
 * Second pass of the entity's own model, the same way a charged creeper draws its energy shell.
 * Added to every living renderer, so players and mobs both get it.
 */
public class TasedChargeLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation CHARGE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    /** Mob effects are not copied onto other clients' effect maps, only the dart entity is. */
    private static final Set<Integer> STUCK = new HashSet<>();

    public static void markStuck(int entityId) {
        STUCK.add(entityId);
    }

    public static void clearStuck(int entityId) {
        STUCK.remove(entityId);
    }

    public static boolean show(LivingEntity entity) {
        return TasedEffect.isTased(entity) || STUCK.contains(entity.getId());
    }

    public TasedChargeLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int packedLight, T entity,
                       float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        if (!show(entity)) {
            return;
        }
        float scroll = (entity.tickCount + partialTick) * 0.01F;
        VertexConsumer consumer = buffer.getBuffer(RenderType.energySwirl(CHARGE, scroll, scroll));
        this.getParentModel().renderToBuffer(pose, consumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY,
                0.55F, 0.85F, 1.0F, 1.0F);
    }
}
