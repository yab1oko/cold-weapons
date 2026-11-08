package yblk.cold_weapons.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.compat.RoleplayItems;

import java.util.List;

/**
 * Melee tool with a charge pool filled by RPM batteries.
 * Inventory: RMB a battery onto the tool, or the tool onto a battery.
 * World: RMB a battery while the tool is in the other hand.
 */
public class ChargedToolItem extends ModMeleeItem {
    public ChargedToolItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    public static boolean isChargedTool(ItemStack stack) {
        return stack.getItem() instanceof ChargedToolItem;
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack tool, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return tryCharge(tool, other, action, player);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack tool, Slot slot, ClickAction action, Player player) {
        return tryCharge(tool, slot.getItem(), action, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        double charge = ToolCharge.get(stack);
        tip.add(Component.translatable("item.cold_weapons.charge", format(charge), format(ToolCharge.MAX)));
        if (ToolCharge.isRunning(stack)) {
            tip.add(Component.translatable("item.cold_weapons.powered"));
        }
        tip.add(Component.translatable("item.cold_weapons.battery_hint"));
    }

    /** Drain 1.5 while a toggled tool stays on. Hold-to-run tools drain from {@code onUseTick}. */
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !drainsWhileCarried() || !ToolCharge.isRunning(stack)) {
            return;
        }
        if (entity.tickCount % 20 != 0) {
            return;
        }
        if (!ToolCharge.drain(stack, ToolCharge.PER_SECOND)) {
            ToolCharge.setRunning(stack, false);
        }
        if (entity instanceof ServerPlayer player) {
            player.inventoryMenu.broadcastChanges();
        }
    }

    protected boolean drainsWhileCarried() {
        return false;
    }

    static boolean tryCharge(ItemStack tool, ItemStack battery, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || battery.isEmpty() || !RoleplayItems.isBattery(battery)) {
            return false;
        }
        if (!isChargedTool(tool) && !(tool.getItem() instanceof TaserItem)) {
            return false;
        }
        return applyBattery(tool, battery, player);
    }

    private static boolean applyBattery(ItemStack tool, ItemStack battery, Player player) {
        if (tool.getItem() instanceof TaserItem) {
            if (!TaserItem.restore(tool)) {
                return false;
            }
        } else if (!isChargedTool(tool) || !ToolCharge.addBattery(tool)) {
            return false;
        }
        if (!player.level().isClientSide && !player.getAbilities().instabuild) {
            battery.shrink(1);
        }
        playCharge(player);
        return true;
    }

    private static void playCharge(Player player) {
        if (player.level().isClientSide) {
            return;
        }
        player.level().playSound(null, player.blockPosition(),
                SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5F, 1.2F);
    }

    static String format(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.05D) {
            return Integer.toString((int) Math.rint(value));
        }
        return String.format(java.util.Locale.ROOT, "%.1f", value);
    }

    @Mod.EventBusSubscriber(modid = ColdWeapons.MODID)
    public static final class ChargeEvents {
        private ChargeEvents() {
        }

        @SubscribeEvent
        public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            ItemStack battery = event.getItemStack();
            if (!RoleplayItems.isBattery(battery)) {
                return;
            }
            Player player = event.getEntity();
            InteractionHand other = event.getHand() == InteractionHand.MAIN_HAND
                    ? InteractionHand.OFF_HAND
                    : InteractionHand.MAIN_HAND;
            ItemStack tool = player.getItemInHand(other);
            if (!applyBattery(tool, battery, player)) {
                return;
            }
            event.setCancellationResult(InteractionResult.sidedSuccess(player.level().isClientSide));
            event.setCanceled(true);
        }
    }
}
