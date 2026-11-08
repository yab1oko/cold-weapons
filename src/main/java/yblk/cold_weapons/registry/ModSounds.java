package yblk.cold_weapons.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ColdWeapons.MODID);

    public static final RegistryObject<SoundEvent> CIRCULAR_START = register("circular_start");
    public static final RegistryObject<SoundEvent> CIRCULAR_IDLE = register("circular_idle");
    public static final RegistryObject<SoundEvent> CIRCULAR_END = register("circular_end");
    public static final RegistryObject<SoundEvent> SAWSWORD_START = register("sawsword_start");
    public static final RegistryObject<SoundEvent> SAWSWORD_IDLE = register("sawsword_idle");
    public static final RegistryObject<SoundEvent> TASER = register("taser");

    private ModSounds() {
    }

    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () ->
                SoundEvent.createVariableRangeEvent(new ResourceLocation(ColdWeapons.MODID, name)));
    }
}
