package yblk.cold_weapons.config;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.item.ThrowWeapons;

/** Shift + hold RMB throws any item listed in the config, same as the kitchen knife. */
@Mod.EventBusSubscriber(modid = ColdWeapons.MODID)
public final class ThrowableWeaponEvents {
    private ThrowableWeaponEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onUse(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (!player.isShiftKeyDown() || !ColdWeaponsConfig.isExtraThrowable(stack) || player.isUsingItem()) {
            return;
        }
        player.startUsingItem(event.getHand());
        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onUseStart(LivingEntityUseItemEvent.Start event) {
        if (ColdWeaponsConfig.isExtraThrowable(event.getItem())) {
            event.setDuration(ThrowWeapons.USE_DURATION);
        }
    }

    @SubscribeEvent
    public static void onUseStop(LivingEntityUseItemEvent.Stop event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player) || !ColdWeaponsConfig.isExtraThrowable(event.getItem())) {
            return;
        }
        int held = ThrowWeapons.USE_DURATION - event.getDuration();
        ThrowWeapons.throwStack(player, event.getItem(), held);
    }
}
