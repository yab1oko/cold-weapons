package yblk.cold_weapons;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import yblk.cold_weapons.config.ColdWeaponsConfig;
import yblk.cold_weapons.network.ModNetwork;
import yblk.cold_weapons.registry.ModCreativeTabs;
import yblk.cold_weapons.registry.ModEffects;
import yblk.cold_weapons.registry.ModEntities;
import yblk.cold_weapons.registry.ModItems;
import yblk.cold_weapons.registry.ModSounds;

@Mod(ColdWeapons.MODID)
public class ColdWeapons {
    public static final String MODID = "cold_weapons";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ColdWeapons() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modBus);
        ModSounds.SOUND_EVENTS.register(modBus);
        ModEffects.EFFECTS.register(modBus);
        ModEntities.ENTITY_TYPES.register(modBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modBus);
        ModNetwork.register();
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ColdWeaponsConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }
}
