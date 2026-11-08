package yblk.cold_weapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/** First/third-person hold pose for the circular saw spin stance. */
public final class MechSawClientExtensions implements IClientItemExtensions {
    public static final MechSawClientExtensions INSTANCE = new MechSawClientExtensions();

    private MechSawClientExtensions() {
    }

    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
        if (entity.isUsingItem() && entity.getUseItem() == stack) {
            return HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
        return HumanoidModel.ArmPose.ITEM;
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm,
                                           ItemStack itemInHand, float partialTick, float equipProcess,
                                           float swingProcess) {
        if (!player.isUsingItem() || player.getUseItem() != itemInHand) {
            return false;
        }
        // Mild adjustment from the default item hold: a bit lower, a bit straighter.
        // Keep this conservative — aggressive offsets push the model off-screen.
        int side = arm == HumanoidArm.RIGHT ? 1 : -1;
        poseStack.translate(side * 0.56F, -0.72F + equipProcess * -0.6F, -0.72F);
        poseStack.mulPose(Axis.XP.rotationDegrees(-12.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(side * 18.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(side * -6.0F));
        return true;
    }
}
