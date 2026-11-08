package yblk.cold_weapons.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum ModToolTiers implements Tier {
    HAMMER(0, 256, 2.0F, 0.0F, 10),
    SLEDGEHAMMER(0, 512, 2.0F, 0.0F, 10),
    JIGSAW(0, 256, 2.0F, 0.0F, 10),
    SCREWDRIVER(0, 250, 2.0F, 0.0F, 10),
    KNIFE(0, 256, 2.0F, 0.0F, 10),
    CLEAVER(0, 260, 2.0F, 0.0F, 10),
    MILITARY_KNIFE(0, 512, 2.0F, 0.0F, 10),
    PAN(0, 312, 2.0F, 0.0F, 10),
    ROLLING_PIN(0, 200, 2.0F, 0.0F, 10),
    RAKE(0, 192, 2.0F, 0.0F, 10),
    BAT(0, 200, 2.0F, 0.0F, 10),
    NAILED_BAT(0, 312, 2.0F, 0.0F, 10),
    /** Stone-tier dig tools: level 1, stone mining speed, 312 durability. */
    STONE_TOOL(1, 312, 4.0F, 0.0F, 5),
    STONE_AXE(0, 200, 2.0F, 0.0F, 5),
    STONE_SPEAR(0, 150, 2.0F, 0.0F, 5),
    SPEAR_KNIFE(0, 220, 2.0F, 0.0F, 8),
    RUSTY_PIPE(0, 180, 2.0F, 0.0F, 5),
    BOTTLE(0, 24, 2.0F, 0.0F, 1),
    WRENCH(0, 358, 2.0F, 0.0F, 10),
    STOP_SIGN(0, 180, 2.0F, 0.0F, 5),
    CROWBAR(0, 256, 2.0F, 0.0F, 10),
    KNUCKLE(0, 192, 2.0F, 0.0F, 8),
    MACHETE(0, 512, 2.0F, 0.0F, 10),
    STEEL_SPEAR(0, 512, 2.0F, 0.0F, 10),
    STEEL_MACE(0, 512, 2.0F, 0.0F, 10),
    SAWSWORD(0, 600, 2.0F, 0.0F, 12),
    MECH_SAW(0, 600, 2.0F, 0.0F, 12);

    private final int level;
    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int enchantmentValue;

    ModToolTiers(int level, int uses, float speed, float attackDamageBonus, int enchantmentValue) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentValue = enchantmentValue;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
}
