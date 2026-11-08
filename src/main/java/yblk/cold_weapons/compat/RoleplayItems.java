package yblk.cold_weapons.compat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

/** Soft lookup for Roleplay Materials items. */
public final class RoleplayItems {
    public static final ResourceLocation BATTERY_ID =
            new ResourceLocation("roleplay_materials", "battery");
    public static final ResourceLocation PLANK_ID =
            new ResourceLocation("roleplay_materials", "plank");

    private RoleplayItems() {
    }

    public static boolean isBattery(ItemStack stack) {
        Item item = battery();
        return item != null && !stack.isEmpty() && stack.is(item);
    }

    public static boolean isPlank(ItemStack stack) {
        Item item = item(PLANK_ID);
        return item != null && !stack.isEmpty() && stack.is(item);
    }

    @Nullable
    private static Item battery() {
        return item(BATTERY_ID);
    }

    @Nullable
    private static Item item(ResourceLocation id) {
        Item item = ForgeRegistries.ITEMS.getValue(id);
        return item != null && item != Items.AIR ? item : null;
    }
}
