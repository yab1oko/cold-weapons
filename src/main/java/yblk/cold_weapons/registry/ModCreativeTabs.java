package yblk.cold_weapons.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ColdWeapons.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = CREATIVE_MODE_TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cold_weapons"))
                    .icon(() -> new ItemStack(ModItems.HAMMER.get()))
                    .displayItems((params, output) -> {
                        output.accept(ModItems.HAMMER.get());
                        output.accept(ModItems.SLEDGEHAMMER.get());
                        output.accept(ModItems.JIGSAW.get());
                        output.accept(ModItems.SCREWDRIVER.get());
                        output.accept(ModItems.KNIFE.get());
                        output.accept(ModItems.CLEAVER.get());
                        output.accept(ModItems.MILITARY_KNIFE.get());
                        output.accept(ModItems.PAN.get());
                        output.accept(ModItems.ROLLING_PIN.get());
                        output.accept(ModItems.RAKE.get());
                        output.accept(ModItems.BAT.get());
                        output.accept(ModItems.NAILED_BAT.get());
                        output.accept(ModItems.STONE_AXE.get());
                        output.accept(ModItems.STONE_SPEAR.get());
                        output.accept(ModItems.SPEAR_KNIFE.get());
                        output.accept(ModItems.PICKAXE.get());
                        output.accept(ModItems.SHOVEL.get());
                        output.accept(ModItems.AXE.get());
                        output.accept(ModItems.RUSTY_PIPE.get());
                        output.accept(ModItems.BOTTLE.get());
                        output.accept(ModItems.WRENCH.get());
                        output.accept(ModItems.STOP_SIGN.get());
                        output.accept(ModItems.CROWBAR.get());
                        output.accept(ModItems.KNUCKLE.get());
                        output.accept(ModItems.MACHETE.get());
                        output.accept(ModItems.STEEL_SPEAR.get());
                        output.accept(ModItems.STEEL_MACE.get());
                        output.accept(ModItems.SAWSWORD.get());
                        output.accept(ModItems.MECH_SAW.get());
                        output.accept(ModItems.TASER.get());
                        output.accept(ModItems.WOOD_SHIELD.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }
}
