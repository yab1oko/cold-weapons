package yblk.cold_weapons.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import yblk.cold_weapons.entity.ThrownKnifeEntity;

/** Shared shift-hold throw used by knives and by ids from the config. */
public final class ThrowWeapons {
    public static final int USE_DURATION = 72000;
    public static final int MIN_CHARGE_TICKS = 5;

    private ThrowWeapons() {
    }

    public static void throwStack(Player player, ItemStack stack, int chargeTicks) {
        if (chargeTicks < MIN_CHARGE_TICKS || stack.isEmpty()) {
            return;
        }
        float charge = Math.min(1.0F, chargeTicks / 20.0F);
        float velocity = 0.9F + charge * 1.0F;
        if (!player.level().isClientSide) {
            ItemStack thrownStack = stack.copyWithCount(1);
            ThrownKnifeEntity knife = new ThrownKnifeEntity(player.level(), player, thrownStack, damageOf(stack));
            knife.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 0.5F);
            player.level().addFreshEntity(knife);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 0.8F, 1.1F);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
    }

    public static float damageOf(ItemStack stack) {
        if (stack.getItem() instanceof ThrowableKnifeItem knife) {
            return knife.getThrowDamage();
        }
        double bonus = 0.0D;
        for (AttributeModifier modifier : stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE)) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                bonus += modifier.getAmount();
            }
        }
        return (float) Math.max(1.0D, 1.0D + bonus);
    }
}
