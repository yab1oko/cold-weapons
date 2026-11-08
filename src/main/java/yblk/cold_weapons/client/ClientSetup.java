package yblk.cold_weapons.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.item.ToolCharge;
import yblk.cold_weapons.registry.ModEntities;
import yblk.cold_weapons.registry.ModItems;

@Mod.EventBusSubscriber(modid = ColdWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ResourceLocation spinning = new ResourceLocation(ColdWeapons.MODID, "spinning");
            ItemProperties.register(ModItems.MECH_SAW.get(), spinning, (stack, level, entity, seed) ->
                    entity != null && entity.isUsingItem() && entity.getUseItem() == stack
                            && ToolCharge.get(stack) > 0.0D ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.SAWSWORD.get(), spinning, (stack, level, entity, seed) ->
                    ToolCharge.isRunning(stack) && ToolCharge.get(stack) > 0.0D ? 1.0F : 0.0F);
            ItemProperties.register(ModItems.WOOD_SHIELD.get(), new ResourceLocation("blocking"),
                    (stack, level, entity, seed) ->
                            entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.THROWN_KNIFE.get(), ThrownKnifeRenderer::new);
        event.registerEntityRenderer(ModEntities.TASER_DART.get(), TaserDartRenderer::new);
    }
}
