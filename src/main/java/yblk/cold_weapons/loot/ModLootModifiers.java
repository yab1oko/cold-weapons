package yblk.cold_weapons.loot;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;

public final class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ColdWeapons.MODID);

    public static final RegistryObject<Codec<ZombieWeaponModifier>> ZOMBIE_WEAPONS =
            SERIALIZERS.register("zombie_weapons", () -> ZombieWeaponModifier.CODEC);

    private ModLootModifiers() {
    }
}
