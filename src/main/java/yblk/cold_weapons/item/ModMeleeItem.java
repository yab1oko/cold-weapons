package yblk.cold_weapons.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/** Sword-like weapon with fractional attack damage (e.g. 6.5 HP). */
public class ModMeleeItem extends SwordItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    /**
     * @param attackDamage total HP damage shown in tooltip (includes the player's base 1)
     * @param attackSpeed  attacks per second (vanilla fist is 4.0)
     */
    public ModMeleeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, 0, attackSpeed - 4.0F, properties);
        this.defaultModifiers = modifiers(attackDamage, attackSpeed);
    }

    protected static Multimap<Attribute, AttributeModifier> modifiers(double attackDamage, double attackSpeed) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                attackDamage - 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                attackSpeed - 4.0D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}
