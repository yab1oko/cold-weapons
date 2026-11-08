package yblk.cold_weapons.registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.effect.TasedEffect;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ColdWeapons.MODID);

    public static final RegistryObject<MobEffect> TASED = EFFECTS.register("tased", TasedEffect::new);

    private ModEffects() {
    }
}
