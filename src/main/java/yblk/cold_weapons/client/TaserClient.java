package yblk.cold_weapons.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.effect.TasedEffect;

/** Light camera shake for a tased local player, and a small jitter on the victim's model. */
@Mod.EventBusSubscriber(modid = ColdWeapons.MODID, value = Dist.CLIENT)
public final class TaserClient {
    private TaserClient() {
    }

    @SubscribeEvent
    public static void shakeCamera(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !TasedEffect.isTased(minecraft.player)) {
            return;
        }
        float time = minecraft.player.tickCount + (float) event.getPartialTick();
        event.setYaw(event.getYaw() + Mth.sin(time * 1.3F) * 0.7F);
        event.setPitch(event.getPitch() + Mth.cos(time * 1.8F) * 0.45F);
        event.setRoll(event.getRoll() + Mth.sin(time * 2.2F) * 1.1F);
    }

    @SubscribeEvent
    public static void livingPre(RenderLivingEvent.Pre<?, ?> event) {
        shake(event.getPoseStack(), event.getEntity(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void livingPost(RenderLivingEvent.Post<?, ?> event) {
        unshake(event.getPoseStack(), event.getEntity());
    }

    @SubscribeEvent
    public static void playerPre(RenderPlayerEvent.Pre event) {
        shake(event.getPoseStack(), event.getEntity(), event.getPartialTick());
    }

    @SubscribeEvent
    public static void playerPost(RenderPlayerEvent.Post event) {
        unshake(event.getPoseStack(), event.getEntity());
    }

    private static void shake(PoseStack pose, LivingEntity entity, float partial) {
        if (!TasedEffect.isTased(entity)) {
            return;
        }
        float time = entity.tickCount + partial;
        pose.pushPose();
        pose.translate(Mth.sin(time * 1.9F) * 0.035F, 0.0D, Mth.cos(time * 1.6F) * 0.035F);
        pose.mulPose(Axis.ZP.rotationDegrees(Mth.sin(time * 2.3F) * 2.5F));
    }

    private static void unshake(PoseStack pose, LivingEntity entity) {
        if (TasedEffect.isTased(entity)) {
            pose.popPose();
        }
    }
}
