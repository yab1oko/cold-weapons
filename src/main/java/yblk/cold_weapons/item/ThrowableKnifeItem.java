package yblk.cold_weapons.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ThrowableKnifeItem extends SwordItem {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    /**
     * @param attackDamage total HP damage shown in tooltip (includes the player's base 1)
     * @param attackSpeed  attacks per second (vanilla fist is 4.0)
     */
    public ThrowableKnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, 0, attackSpeed - 4.0F, properties);
        this.attackDamage = attackDamage;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                attackDamage - 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                attackSpeed - 4.0D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public float getThrowDamage() {
        return this.attackDamage;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return;
        }
        int chargeTicks = this.getUseDuration(stack) - timeLeft;
        ThrowWeapons.throwStack(player, stack, chargeTicks);
    }
}
