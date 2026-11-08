package yblk.cold_weapons.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import yblk.cold_weapons.entity.TaserDartEntity;
import yblk.cold_weapons.registry.ModSounds;

import java.util.List;

/** Right-click fires two threads. A hit stuns for 5 seconds, then the taser recharges for 18. */
public class TaserItem extends Item {
    public static final int MAX_DURABILITY = 100;
    /** One RPM battery refills the whole cell, same step Fashion uses for NVG. */
    public static final int BATTERY_RESTORE = 100;
    public static final int COOLDOWN_TICKS = 18 * 20;
    public static final double RANGE = 8.0D;
    private static final String TAG_READY = "TaserReady";

    public TaserItem(Properties properties) {
        super(properties.durability(MAX_DURABILITY));
    }

    public static boolean restore(ItemStack stack) {
        int damage = stack.getDamageValue();
        if (damage <= 0) {
            return false;
        }
        stack.setDamageValue(Math.max(0, damage - BATTERY_RESTORE));
        return true;
    }

    /** 0 just fired, 1 ready. */
    public static float cooldownFill(ItemStack stack, long gameTime) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TAG_READY)) {
            return 1.0F;
        }
        long until = tag.getLong(TAG_READY);
        if (gameTime >= until) {
            return 1.0F;
        }
        return 1.0F - (until - gameTime) / (float) COOLDOWN_TICKS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage() || !isReady(stack, level)) {
            return InteractionResultHolder.fail(stack);
        }
        if (!level.isClientSide && level instanceof ServerLevel server) {
            fire(server, player, hand);
            stack.setDamageValue(stack.getDamageValue() + 1);
        }
        stack.getOrCreateTag().putLong(TAG_READY, level.getGameTime() + COOLDOWN_TICKS);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.inventoryMenu.broadcastChanges();
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean overrideOtherStackedOnMe(
            ItemStack taser, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return ChargedToolItem.tryCharge(taser, other, action, player);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack taser, Slot slot, ClickAction action, Player player) {
        return ChargedToolItem.tryCharge(taser, slot.getItem(), action, player);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        int left = stack.getMaxDamage() - stack.getDamageValue();
        tip.add(Component.translatable("item.cold_weapons.taser.durability", left, stack.getMaxDamage()));
        tip.add(Component.translatable("item.cold_weapons.battery_hint"));
    }

    private static boolean isReady(ItemStack stack, Level level) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TAG_READY)) {
            return true;
        }
        return level.getGameTime() >= tag.getLong(TAG_READY);
    }

    private static void fire(ServerLevel level, Player player, InteractionHand hand) {
        Vec3 look = player.getLookAngle();
        Vec3 side = look.cross(new Vec3(0.0D, 1.0D, 0.0D));
        if (side.lengthSqr() < 1.0E-4D) {
            side = new Vec3(1.0D, 0.0D, 0.0D);
        }
        side = side.normalize();
        int sign = player.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT ? 1 : -1;
        if (hand != InteractionHand.MAIN_HAND) {
            sign = -sign;
        }
        Vec3 muzzle = player.getEyePosition()
                .add(side.scale(0.32D * sign))
                .add(0.0D, -0.28D, 0.0D)
                .add(look.scale(0.45D));
        TaserDartEntity dart = new TaserDartEntity(level, player, hand == InteractionHand.MAIN_HAND);
        dart.setPos(muzzle);
        dart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.15F, 0.35F);
        level.addFreshEntity(dart);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.TASER.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
