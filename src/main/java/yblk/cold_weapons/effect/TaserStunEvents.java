package yblk.cold_weapons.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yblk.cold_weapons.ColdWeapons;

/** While tased: no attacks, no item use, and movement is crushed to a crawl. */
@Mod.EventBusSubscriber(modid = ColdWeapons.MODID)
public final class TaserStunEvents {
    private TaserStunEvents() {
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!TasedEffect.isTased(entity)) {
            return;
        }
        entity.setSprinting(false);
        entity.stopUsingItem();
        if (entity instanceof Mob mob) {
            mob.setTarget(null);
            mob.setAggressive(false);
            mob.getNavigation().stop();
            mob.setXxa(0.0F);
            mob.setYya(0.0F);
            mob.setZza(0.0F);
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        var source = event.getSource();
        if (attackerTased(source.getEntity()) || attackerTased(source.getDirectEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRetarget(LivingChangeTargetEvent event) {
        if (TasedEffect.isTased(event.getEntity()) && event.getNewTarget() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        if (TasedEffect.isTased(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        var source = event.getSource();
        if (attackerTased(source.getEntity()) || attackerTased(source.getDirectEntity())) {
            event.setCanceled(true);
        }
    }

    private static boolean attackerTased(net.minecraft.world.entity.Entity entity) {
        return entity instanceof LivingEntity living && TasedEffect.isTased(living);
    }

    @SubscribeEvent
    public static void onUseItem(PlayerInteractEvent.RightClickItem event) {
        block(event);
    }

    @SubscribeEvent
    public static void onUseBlock(PlayerInteractEvent.RightClickBlock event) {
        block(event);
    }

    @SubscribeEvent
    public static void onSwingBlock(PlayerInteractEvent.LeftClickBlock event) {
        block(event);
    }

    @SubscribeEvent
    public static void onUseEntity(PlayerInteractEvent.EntityInteract event) {
        block(event);
    }

    private static void block(PlayerInteractEvent event) {
        if (TasedEffect.isTased(event.getEntity())) {
            event.setCanceled(true);
        }
    }
}
