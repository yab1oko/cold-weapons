package yblk.cold_weapons.registry;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.item.MechSawItem;
import yblk.cold_weapons.item.SawSwordItem;
import yblk.cold_weapons.item.TaserItem;
import yblk.cold_weapons.item.ModMeleeItem;
import yblk.cold_weapons.item.ModToolTiers;
import yblk.cold_weapons.item.ThrowableKnifeItem;
import yblk.cold_weapons.item.WoodShieldItem;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ColdWeapons.MODID);

    /**
     * SwordItem damage = 1 (base) + attackDamage + tier bonus.
     * attackDamage 4 + tier 0 → 5 HP. Attack speed modifier -2.4 → 4.0 - 2.4 = 1.6.
     */
    public static final RegistryObject<Item> HAMMER = ITEMS.register("hammer",
            () -> new SwordItem(ModToolTiers.HAMMER, 4, -2.4F, new Item.Properties()));

    /** 8 HP, attack speed 0.9 (4.0 - 3.1), durability 512. */
    public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer",
            () -> new SwordItem(ModToolTiers.SLEDGEHAMMER, 7, -3.1F, new Item.Properties()));

    /** 4 HP, attack speed 1.3 (4.0 - 2.7), durability 256. */
    public static final RegistryObject<Item> JIGSAW = ITEMS.register("jigsaw",
            () -> new SwordItem(ModToolTiers.JIGSAW, 3, -2.7F, new Item.Properties()));

    /** 3 HP, attack speed 2.0 (4.0 - 2.0), durability 250. */
    public static final RegistryObject<Item> SCREWDRIVER = ITEMS.register("screwdriver",
            () -> new SwordItem(ModToolTiers.SCREWDRIVER, 2, -2.0F, new Item.Properties()));

    /** Kitchen knife: 5 HP, attack speed 2.0, durability 256. Throwable. */
    public static final RegistryObject<Item> KNIFE = ITEMS.register("knife",
            () -> new ThrowableKnifeItem(ModToolTiers.KNIFE, 5.0F, 2.0F, new Item.Properties()));

    /** Cleaver: 5.5 HP, attack speed 2.0, durability 260. Throwable. */
    public static final RegistryObject<Item> CLEAVER = ITEMS.register("cleaver",
            () -> new ThrowableKnifeItem(ModToolTiers.CLEAVER, 5.5F, 2.0F, new Item.Properties()));

    /** Military knife: 6 HP, attack speed 2.0, durability 512. Throwable. */
    public static final RegistryObject<Item> MILITARY_KNIFE = ITEMS.register("military_knife",
            () -> new ThrowableKnifeItem(ModToolTiers.MILITARY_KNIFE, 6.0F, 2.0F, new Item.Properties()));

    /** Frying pan: 6 HP, attack speed 1.2 (4.0 - 2.8), durability 312. */
    public static final RegistryObject<Item> PAN = ITEMS.register("pan",
            () -> new SwordItem(ModToolTiers.PAN, 5, -2.8F, new Item.Properties()));

    /** Rolling pin: 4 HP, attack speed 1.6 (4.0 - 2.4), durability 200. */
    public static final RegistryObject<Item> ROLLING_PIN = ITEMS.register("rolling_pin",
            () -> new SwordItem(ModToolTiers.ROLLING_PIN, 3, -2.4F, new Item.Properties()));

    /** Rake: 6 HP, attack speed 1.0, durability 192. */
    public static final RegistryObject<Item> RAKE = ITEMS.register("rake",
            () -> new ModMeleeItem(ModToolTiers.RAKE, 6.0F, 1.0F, new Item.Properties()));

    /** Baseball bat: 5 HP, attack speed 1.6, durability 200. */
    public static final RegistryObject<Item> BAT = ITEMS.register("bat",
            () -> new ModMeleeItem(ModToolTiers.BAT, 5.0F, 1.6F, new Item.Properties()));

    /** Nailed bat: 7 HP, attack speed 1.6 (4.0 - 2.4), durability 312. */
    public static final RegistryObject<Item> NAILED_BAT = ITEMS.register("nailed_bat",
            () -> new SwordItem(ModToolTiers.NAILED_BAT, 6, -2.4F, new Item.Properties()));

    /** Stone axe: 6 HP, attack speed 1.1, durability 200. */
    public static final RegistryObject<Item> STONE_AXE = ITEMS.register("stone_axe",
            () -> new ModMeleeItem(ModToolTiers.STONE_AXE, 6.0F, 1.1F, new Item.Properties()));

    /** Stone spear: 6.5 HP, attack speed 1.0, durability 150. */
    public static final RegistryObject<Item> STONE_SPEAR = ITEMS.register("stone_spear",
            () -> new ModMeleeItem(ModToolTiers.STONE_SPEAR, 6.5F, 1.0F, new Item.Properties()));

    /** Knife spear: 7 HP, attack speed 1.0, durability 220. */
    public static final RegistryObject<Item> SPEAR_KNIFE = ITEMS.register("spear_knife",
            () -> new ModMeleeItem(ModToolTiers.SPEAR_KNIFE, 7.0F, 1.0F, new Item.Properties()));

    /** Pickaxe: 5 HP, attack speed 1.0, durability 312, stone-tier mining. */
    public static final RegistryObject<Item> PICKAXE = ITEMS.register("pickaxe",
            () -> new PickaxeItem(ModToolTiers.STONE_TOOL, 4, -3.0F, new Item.Properties()));

    /** Shovel: 5 HP, attack speed 1.0, durability 312, stone-tier digging. */
    public static final RegistryObject<Item> SHOVEL = ITEMS.register("shovel",
            () -> new ShovelItem(ModToolTiers.STONE_TOOL, 4.0F, -3.0F, new Item.Properties()));

    /** Axe: 7 HP, attack speed 1.1, durability 312, stone-tier chopping. */
    public static final RegistryObject<Item> AXE = ITEMS.register("axe",
            () -> new AxeItem(ModToolTiers.STONE_TOOL, 6.0F, -2.9F, new Item.Properties()));

    /** Rusty pipe: 5 HP, attack speed 1.2, durability 180. */
    public static final RegistryObject<Item> RUSTY_PIPE = ITEMS.register("rusty_pipe",
            () -> new SwordItem(ModToolTiers.RUSTY_PIPE, 4, -2.8F, new Item.Properties()));

    /** Broken bottle: 4 HP, attack speed 1.2, durability 24. */
    public static final RegistryObject<Item> BOTTLE = ITEMS.register("bottle",
            () -> new SwordItem(ModToolTiers.BOTTLE, 3, -2.8F, new Item.Properties()));

    /** Adjustable wrench: 7 HP, attack speed 1.0, durability 358. */
    public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench",
            () -> new ModMeleeItem(ModToolTiers.WRENCH, 7.0F, 1.0F, new Item.Properties()));

    /** Stop sign: 7.5 HP, attack speed 0.9, durability 180. Two-handed. */
    public static final RegistryObject<Item> STOP_SIGN = ITEMS.register("stop_sign",
            () -> new ModMeleeItem(ModToolTiers.STOP_SIGN, 7.5F, 0.9F, new Item.Properties()));

    /** Crowbar: 5.5 HP, attack speed 1.5, durability 256. Pickaxe tag only (no digging). */
    public static final RegistryObject<Item> CROWBAR = ITEMS.register("crowbar",
            () -> new ModMeleeItem(ModToolTiers.CROWBAR, 5.5F, 1.5F, new Item.Properties()));

    /** Knuckles: 3.5 HP, attack speed 2.0, durability 192. */
    public static final RegistryObject<Item> KNUCKLE = ITEMS.register("knuckle",
            () -> new ModMeleeItem(ModToolTiers.KNUCKLE, 3.5F, 2.0F, new Item.Properties()));

    /** Machete: 7 HP, attack speed 1.6, durability 512. */
    public static final RegistryObject<Item> MACHETE = ITEMS.register("machete",
            () -> new ModMeleeItem(ModToolTiers.MACHETE, 7.0F, 1.6F, new Item.Properties()));

    /** Forged steel spear: 8 HP, attack speed 1.1, durability 512. */
    public static final RegistryObject<Item> STEEL_SPEAR = ITEMS.register("steel_spear",
            () -> new ModMeleeItem(ModToolTiers.STEEL_SPEAR, 8.0F, 1.1F, new Item.Properties()));

    /** Forged steel mace: 7 HP, attack speed 1.6, durability 512. */
    public static final RegistryObject<Item> STEEL_MACE = ITEMS.register("steel_mace",
            () -> new ModMeleeItem(ModToolTiers.STEEL_MACE, 7.0F, 1.6F, new Item.Properties()));

    /** Chainsaw sword: 4 HP idle, 8 HP while running. RMB toggles the blade. */
    public static final RegistryObject<Item> SAWSWORD = ITEMS.register("sawsword",
            () -> new SawSwordItem(ModToolTiers.SAWSWORD, new Item.Properties()));

    /** Circular saw: 8 HP, attack speed 0.9, durability 600. Hold RMB to spin. */
    public static final RegistryObject<Item> MECH_SAW = ITEMS.register("mech_saw",
            () -> new MechSawItem(ModToolTiers.MECH_SAW, 8.0F, 0.9F, new Item.Properties()));

    /** Taser: 100 durability, restored by batteries. 18s reload, 5s stun. */
    public static final RegistryObject<Item> TASER = ITEMS.register("taser",
            () -> new TaserItem(new Item.Properties()));

    /** Wooden shield: blocks like the vanilla shield, half the durability (168). */
    public static final RegistryObject<Item> WOOD_SHIELD = ITEMS.register("wood_shield",
            () -> new WoodShieldItem(new Item.Properties()));

    private ModItems() {
    }
}
