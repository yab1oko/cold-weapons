package yblk.cold_weapons.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.entity.ThrownKnifeEntity;
import yblk.cold_weapons.entity.TaserDartEntity;

public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ColdWeapons.MODID);

    public static final RegistryObject<EntityType<ThrownKnifeEntity>> THROWN_KNIFE =
            ENTITY_TYPES.register("thrown_knife", () -> EntityType.Builder
                    .<ThrownKnifeEntity>of(ThrownKnifeEntity::new, MobCategory.MISC)
                    .sized(0.35F, 0.35F)
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build("thrown_knife"));

    public static final RegistryObject<EntityType<TaserDartEntity>> TASER_DART =
            ENTITY_TYPES.register("taser_dart", () -> EntityType.Builder
                    .<TaserDartEntity>of(TaserDartEntity::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(6)
                    .updateInterval(1)
                    .build("taser_dart"));

    private ModEntities() {
    }
}
