package yblk.cold_weapons.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.registry.ModItems;

/** Loaded by JEI only. A missing JEI install never references this class. */
@JeiPlugin
public class ColdWeaponsJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ColdWeapons.MODID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        found(registration, ModItems.HAMMER, "hammer", "melee");
        found(registration, ModItems.SLEDGEHAMMER, "sledgehammer", "melee");
        found(registration, ModItems.JIGSAW, "jigsaw", "melee");
        found(registration, ModItems.SCREWDRIVER, "screwdriver", "melee");
        found(registration, ModItems.KNIFE, "knife", "throw");
        found(registration, ModItems.CLEAVER, "cleaver", "throw");
        found(registration, ModItems.MILITARY_KNIFE, "military_knife", "throw");
        found(registration, ModItems.PAN, "pan", "melee");
        found(registration, ModItems.ROLLING_PIN, "rolling_pin", "melee");
        found(registration, ModItems.RAKE, "rake", "melee");
        crafted(registration, ModItems.BAT, "bat", "melee");
        rpm(registration, ModItems.NAILED_BAT, "nailed_bat", "melee");
        crafted(registration, ModItems.STONE_AXE, "stone_axe", "tool");
        crafted(registration, ModItems.STONE_SPEAR, "stone_spear", "melee");
        rpm(registration, ModItems.SPEAR_KNIFE, "spear_knife", "melee");
        found(registration, ModItems.PICKAXE, "pickaxe", "tool");
        found(registration, ModItems.SHOVEL, "shovel", "tool");
        found(registration, ModItems.AXE, "axe", "tool");
        found(registration, ModItems.RUSTY_PIPE, "rusty_pipe", "melee");
        found(registration, ModItems.BOTTLE, "bottle", "melee");
        found(registration, ModItems.WRENCH, "wrench", "melee");
        found(registration, ModItems.STOP_SIGN, "stop_sign", "melee");
        found(registration, ModItems.CROWBAR, "crowbar", "melee");
        found(registration, ModItems.KNUCKLE, "knuckle", "melee");
        rpm(registration, ModItems.MACHETE, "machete", "melee");
        rpm(registration, ModItems.STEEL_SPEAR, "steel_spear", "melee");
        rpm(registration, ModItems.STEEL_MACE, "steel_mace", "melee");
        rpm(registration, ModItems.SAWSWORD, "sawsword", "sawsword");
        rpm(registration, ModItems.MECH_SAW, "mech_saw", "saw");
        found(registration, ModItems.TASER, "taser", "taser");
        rpm(registration, ModItems.WOOD_SHIELD, "wood_shield", "shield");
    }

    private static void found(IRecipeRegistration registration, RegistryObject<Item> item, String id, String use) {
        page(registration, item, id, "found", use);
    }

    private static void rpm(IRecipeRegistration registration, RegistryObject<Item> item, String id, String use) {
        page(registration, item, id, "found_rpm", use);
    }

    private static void crafted(IRecipeRegistration registration, RegistryObject<Item> item, String id, String use) {
        page(registration, item, id, "found_craft", use);
    }

    private static void page(IRecipeRegistration registration, RegistryObject<Item> item, String id, String obtain, String use) {
        registration.addIngredientInfo(item.get(),
                Component.translatable("jei.cold_weapons.about." + id),
                Component.translatable("jei.cold_weapons." + obtain),
                Component.translatable("jei.cold_weapons.use." + use),
                Component.translatable("jei.cold_weapons.bc"));
    }
}
