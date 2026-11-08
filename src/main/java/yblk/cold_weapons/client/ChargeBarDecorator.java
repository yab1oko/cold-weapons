package yblk.cold_weapons.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.item.ToolCharge;
import yblk.cold_weapons.item.TaserItem;
import yblk.cold_weapons.registry.ModItems;

/** Light-blue charge bar drawn just above the durability bar. */
@Mod.EventBusSubscriber(modid = ColdWeapons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ChargeBarDecorator {
    private static final int BLUE = 0xFF4DA3FF;

    private ChargeBarDecorator() {
    }

    @SubscribeEvent
    public static void register(RegisterItemDecorationsEvent event) {
        event.register(ModItems.MECH_SAW.get(), ChargeBarDecorator::render);
        event.register(ModItems.SAWSWORD.get(), ChargeBarDecorator::render);
        event.register(ModItems.TASER.get(), ChargeBarDecorator::renderTaser);
    }

    private static boolean render(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
        int filled = Math.round((float) (ToolCharge.get(stack) / ToolCharge.MAX * 13.0D));
        if (ToolCharge.get(stack) > 0.0D) {
            filled = Math.max(1, Math.min(13, filled));
        }
        int x0 = x + 2;
        int y0 = y + 11;
        graphics.fill(RenderType.guiOverlay(), x0, y0, x0 + 13, y0 + 2, 0xFF000000);
        if (filled > 0) {
            graphics.fill(RenderType.guiOverlay(), x0, y0, x0 + filled, y0 + 1, BLUE);
        }
        return false;
    }

    /** Yellow reload bar. Hidden once the 18 second charge is full. */
    private static boolean renderTaser(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
        if (MinecraftLevel() == null) {
            return false;
        }
        float fill = TaserItem.cooldownFill(stack, net.minecraft.client.Minecraft.getInstance().level.getGameTime());
        if (fill >= 1.0F) {
            return false;
        }
        int filled = Math.max(1, Math.min(13, Math.round(fill * 13.0F)));
        int x0 = x + 2;
        int y0 = y + 11;
        graphics.fill(RenderType.guiOverlay(), x0, y0, x0 + 13, y0 + 2, 0xFF000000);
        graphics.fill(RenderType.guiOverlay(), x0, y0, x0 + filled, y0 + 1, 0xFFFFD15A);
        return false;
    }

    private static net.minecraft.world.level.Level MinecraftLevel() {
        return net.minecraft.client.Minecraft.getInstance().level;
    }
}
