package yblk.cold_weapons.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import yblk.cold_weapons.item.ThrowableKnifeItem;

import java.util.List;

/** Common config. Values are read when used, so a reload applies without restarting the items. */
public final class ColdWeaponsConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> THROWABLE_WEAPONS;
    private static final ForgeConfigSpec.DoubleValue HEADSHOT_MULTIPLIER;
    private static final ForgeConfigSpec.DoubleValue TASER_RANGE;
    private static final ForgeConfigSpec.IntValue TASER_STUN_SECONDS;
    private static final ForgeConfigSpec.IntValue TASER_COOLDOWN_SECONDS;
    private static final ForgeConfigSpec.IntValue TASER_DURABILITY;
    private static final ForgeConfigSpec.DoubleValue SAW_SPIN_DAMAGE;
    private static final ForgeConfigSpec.IntValue SAW_SPIN_INTERVAL;
    private static final ForgeConfigSpec.DoubleValue SAWSWORD_IDLE_DAMAGE;
    private static final ForgeConfigSpec.DoubleValue SAWSWORD_POWERED_DAMAGE;
    private static final ForgeConfigSpec.DoubleValue CHARGE_MAX;
    private static final ForgeConfigSpec.DoubleValue CHARGE_DRAIN;
    private static final ForgeConfigSpec.DoubleValue BATTERY_RESTORE;
    private static final ForgeConfigSpec.BooleanValue ZOMBIE_DROPS;
    private static final ForgeConfigSpec.DoubleValue ZOMBIE_CHANCE;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ZOMBIE_ITEMS;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ZOMBIE_ENTITIES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment(
                "Weapons that throw like the kitchen knife.",
                "Hold Shift and right-click, then release to throw.",
                "Write a full id, for example minecraft:iron_sword or another_mod:dagger.",
                "Knives from this mod already throw. Do not list them.",
                "Оружия, которые кидаются как кухонный нож. Ножи этого мода сюда писать не нужно."
        );
        THROWABLE_WEAPONS = builder.defineListAllowEmpty(
                "throwableWeapons",
                List.of(),
                ColdWeaponsConfig::isItemId);

        builder.push("thrown");
        builder.comment(
                "Damage multiplier when a thrown weapon hits the head.",
                "Множитель урона при попадании в голову."
        );
        HEADSHOT_MULTIPLIER = builder.defineInRange("headshotMultiplier", 2.0D, 1.0D, 20.0D);
        builder.pop();

        builder.push("taser");
        builder.comment("How far the dart and wires stay connected, in blocks.", "Дальность дротика и проводов, в блоках.");
        TASER_RANGE = builder.defineInRange("range", 8.0D, 1.0D, 64.0D);
        builder.comment("How long a hit stuns the target, in seconds.", "Длительность оглушения, в секундах.");
        TASER_STUN_SECONDS = builder.defineInRange("stunSeconds", 5, 1, 60);
        builder.comment("Reload time after a shot, in seconds.", "Перезарядка после выстрела, в секундах.");
        TASER_COOLDOWN_SECONDS = builder.defineInRange("cooldownSeconds", 18, 1, 120);
        builder.comment("Durability. One shot spends one point.", "Прочность. Один выстрел тратит одно деление.");
        TASER_DURABILITY = builder.defineInRange("maxDurability", 100, 1, 10000);
        builder.pop();

        builder.push("circularSaw");
        builder.comment("Damage dealt on each spin tick that lands.", "Урон за каждый тик вращения, который попал.");
        SAW_SPIN_DAMAGE = builder.defineInRange("spinDamage", 0.5D, 0.0D, 40.0D);
        builder.comment("Ticks between spin hits. 1 hits every tick, which is the default.", "Тики между попаданиями. 1 — каждый тик.");
        SAW_SPIN_INTERVAL = builder.defineInRange("spinIntervalTicks", 1, 1, 40);
        builder.pop();

        builder.push("chainsawSword");
        builder.comment("Damage with the blade off.", "Урон с выключенным полотном.");
        SAWSWORD_IDLE_DAMAGE = builder.defineInRange("idleDamage", 4.0D, 0.0D, 40.0D);
        builder.comment("Damage while the blade is running.", "Урон с включённым полотном.");
        SAWSWORD_POWERED_DAMAGE = builder.defineInRange("poweredDamage", 8.0D, 0.0D, 40.0D);
        builder.pop();

        builder.push("charge");
        builder.comment("Charge pool on the circular saw and chainsaw sword.", "Запас заряда циркулярки и бензомеча.");
        CHARGE_MAX = builder.defineInRange("max", 150.0D, 1.0D, 100000.0D);
        builder.comment("Charge spent each second the tool is running.", "Заряд в секунду, пока инструмент работает.");
        CHARGE_DRAIN = builder.defineInRange("drainPerSecond", 1.5D, 0.0D, 1000.0D);
        builder.comment(
                "Charge restored by one Roleplay Materials battery. The taser uses the same amount as durability.",
                "Сколько заряда даёт одна батарея Roleplay Materials. Тазер получает столько же прочности."
        );
        BATTERY_RESTORE = builder.defineInRange("batteryRestore", 100.0D, 1.0D, 100000.0D);
        builder.pop();

        builder.push("zombieDrops");
        builder.comment(
                "Rare scavenged-weapon drops from zombies. Off until a pack turns it on. Not chest loot.",
                "Редкий дроп хлама с зомби. Выключено, пока это не включат. Не лут сундуков."
        );
        ZOMBIE_DROPS = builder.define("enabled", false);
        builder.comment("Chance on a player kill, after the entity matches the list.", "Шанс при убийстве игроком, если тип моба в списке.");
        ZOMBIE_CHANCE = builder.defineInRange("chance", 0.02D, 0.0D, 1.0D);
        builder.comment("Item ids that may drop. Junk only.", "Id предметов. Только хлам.");
        ZOMBIE_ITEMS = builder.defineListAllowEmpty("items", List.of(
                "cold_weapons:rusty_pipe",
                "cold_weapons:bottle",
                "cold_weapons:knife",
                "cold_weapons:crowbar",
                "cold_weapons:bat",
                "cold_weapons:screwdriver",
                "cold_weapons:rolling_pin",
                "cold_weapons:pan"
        ), ColdWeaponsConfig::isItemId);
        builder.comment("Entity type ids that can drop the list.", "Типы существ, с которых может выпасть список.");
        ZOMBIE_ENTITIES = builder.defineListAllowEmpty("entities", List.of(
                "minecraft:zombie",
                "minecraft:zombie_villager",
                "minecraft:husk",
                "minecraft:drowned"
        ), ColdWeaponsConfig::isItemId);
        builder.pop();

        SPEC = builder.build();
    }

    private ColdWeaponsConfig() {
    }

    public static double headshotMultiplier() {
        return loaded(HEADSHOT_MULTIPLIER, 2.0D);
    }

    public static double taserRange() {
        return loaded(TASER_RANGE, 8.0D);
    }

    public static int taserStunTicks() {
        return loaded(TASER_STUN_SECONDS, 5) * 20;
    }

    public static int taserStunSeconds() {
        return loaded(TASER_STUN_SECONDS, 5);
    }

    public static int taserCooldownTicks() {
        return loaded(TASER_COOLDOWN_SECONDS, 18) * 20;
    }

    public static int taserCooldownSeconds() {
        return loaded(TASER_COOLDOWN_SECONDS, 18);
    }

    public static int taserDurability() {
        return loaded(TASER_DURABILITY, 100);
    }

    public static float sawSpinDamage() {
        return (float) loaded(SAW_SPIN_DAMAGE, 0.5D);
    }

    public static int sawSpinInterval() {
        return loaded(SAW_SPIN_INTERVAL, 1);
    }

    public static float sawswordIdleDamage() {
        return (float) loaded(SAWSWORD_IDLE_DAMAGE, 4.0D);
    }

    public static float sawswordPoweredDamage() {
        return (float) loaded(SAWSWORD_POWERED_DAMAGE, 8.0D);
    }

    public static double chargeMax() {
        return loaded(CHARGE_MAX, 150.0D);
    }

    public static double chargeDrainPerSecond() {
        return loaded(CHARGE_DRAIN, 1.5D);
    }

    public static double batteryRestore() {
        return loaded(BATTERY_RESTORE, 100.0D);
    }

    public static boolean zombieDropsEnabled() {
        return SPEC.isLoaded() && ZOMBIE_DROPS.get();
    }

    public static double zombieDropChance() {
        return loaded(ZOMBIE_CHANCE, 0.02D);
    }

    public static List<? extends String> zombieDropItems() {
        return SPEC.isLoaded() ? ZOMBIE_ITEMS.get() : List.of();
    }

    public static List<? extends String> zombieDropEntities() {
        return SPEC.isLoaded() ? ZOMBIE_ENTITIES.get() : List.of();
    }

    public static boolean isExtraThrowable(ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof ThrowableKnifeItem || !SPEC.isLoaded()) {
            return false;
        }
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (id == null) {
            return false;
        }
        for (String raw : THROWABLE_WEAPONS.get()) {
            if (raw == null) {
                continue;
            }
            String text = raw.trim();
            if (text.isEmpty()) {
                continue;
            }
            ResourceLocation parsed = ResourceLocation.tryParse(text.contains(":") ? text : "minecraft:" + text);
            if (id.equals(parsed)) {
                return true;
            }
        }
        return false;
    }

    private static double loaded(ForgeConfigSpec.DoubleValue value, double fallback) {
        return SPEC.isLoaded() ? value.get() : fallback;
    }

    private static int loaded(ForgeConfigSpec.IntValue value, int fallback) {
        return SPEC.isLoaded() ? value.get() : fallback;
    }

    private static boolean isItemId(Object value) {
        if (!(value instanceof String text)) {
            return false;
        }
        String id = text.trim();
        if (id.isEmpty()) {
            return false;
        }
        return ResourceLocation.isValidResourceLocation(id.contains(":") ? id : "minecraft:" + id);
    }
}
