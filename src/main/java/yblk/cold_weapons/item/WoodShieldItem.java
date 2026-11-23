package yblk.cold_weapons.item;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import yblk.cold_weapons.compat.RoleplayItems;

/** Wooden shield. Vanilla shield lasts 336 uses; this one lasts half of that. */
public class WoodShieldItem extends ShieldItem {
    public static final int DURABILITY = 168;

    public WoodShieldItem(Properties properties) {
        super(properties.durability(DURABILITY));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repair) {
        if (RoleplayItems.isPlank(repair)) {
            return true;
        }
        if (!RoleplayItems.hasPlank() && repair.is(ItemTags.PLANKS)) {
            return true;
        }
        return super.isValidRepairItem(stack, repair);
    }
}
