package yblk.cold_weapons.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import yblk.cold_weapons.item.ThrowableKnifeItem;

import java.util.List;

/** Common config: extra items that throw like the kitchen knife. */
public final class ColdWeaponsConfig {
    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> THROWABLE_WEAPONS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment(
                "Оружия, которые кидаются как кухонный нож.",
                "Зажми Shift и ПКМ, подержи и отпусти — предмет полетит.",
                "Пиши полный id, например minecraft:iron_sword или another_mod:dagger.",
                "Ножи этого мода уже кидаются сами, их сюда писать не нужно."
        );
        THROWABLE_WEAPONS = builder.defineListAllowEmpty(
                "throwableWeapons",
                List.of(),
                ColdWeaponsConfig::isItemId);
        SPEC = builder.build();
    }

    private ColdWeaponsConfig() {
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
