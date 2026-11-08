package yblk.cold_weapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import yblk.cold_weapons.entity.ThrownKnifeEntity;

public class ThrownKnifeRenderer extends EntityRenderer<ThrownKnifeEntity> {
    private final ItemRenderer itemRenderer;

    public ThrownKnifeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ThrownKnifeEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        ItemStack stack = entity.getKnifeItem();
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch));

        if (!entity.isInGround()) {
            poseStack.translate(0.0D, -0.22D, 0.0D);
            float spin = (entity.tickCount + partialTick) * 48.0F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(spin));
        } else {
            // Tip into the hit surface, freeze spin.
            poseStack.mulPose(Axis.ZP.rotationDegrees(-90.0F));
        }

        poseStack.scale(0.85F, 0.85F, 0.85F);
        this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight,
                OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownKnifeEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
