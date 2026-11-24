package yblk.cold_weapons.item;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.registry.ModItems;

/** Quiet material impact on a melee hit, plus a shatter when the bottle breaks. */
@Mod.EventBusSubscriber(modid = ColdWeapons.MODID)
public final class MaterialHits {
    private MaterialHits() {
    }

    @SubscribeEvent
    public static void onMeleeHit(LivingHurtEvent event) {
        if (event.getAmount() <= 0.0F || event.getEntity().level().isClientSide) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }
        if (event.getSource().getDirectEntity() instanceof Projectile) {
            return;
        }
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof MechSawItem && player.isUsingItem()) {
            return;
        }
        var sound = impact(stack.getItem());
        if (sound == null) {
            return;
        }
        LivingEntity target = event.getEntity();
        float pitch = 0.85F + target.getRandom().nextFloat() * 0.15F;
        target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                sound, SoundSource.PLAYERS, 1.0F, pitch);
    }

    @SubscribeEvent
    public static void onBreak(PlayerDestroyItemEvent event) {
        ItemStack broken = event.getOriginal();
        if (broken.getItem() != ModItems.BOTTLE.get() || event.getEntity().level().isClientSide) {
            return;
        }
        shatter(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getEyeY(), event.getEntity().getZ());
    }

    public static void shatter(Level level, double x, double y, double z) {
        level.playSound(null, x, y, z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.7F, 0.95F + level.getRandom().nextFloat() * 0.1F);
        if (level instanceof ServerLevel server) {
            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.GLASS.defaultBlockState()),
                    x, y, z, 10, 0.15D, 0.15D, 0.15D, 0.05D);
        }
    }

    public static boolean isBottle(ItemStack stack) {
        return stack.is(ModItems.BOTTLE.get());
    }

    private static net.minecraft.sounds.SoundEvent impact(Item item) {
        var id = ForgeRegistries.ITEMS.getKey(item);
        String path = id == null ? "" : id.getPath();
        boolean ours = id != null && ColdWeapons.MODID.equals(id.getNamespace());
        if (item == ModItems.BAT.get()
                || item == ModItems.NAILED_BAT.get()
                || item == ModItems.ROLLING_PIN.get()
                || item == ModItems.KNUCKLE.get()
                || item == ModItems.STOP_SIGN.get()
                || (ours && (path.contains("rolling_pin") || path.contains("bat")
                || path.contains("knuckle") || path.contains("stop_sign")))) {
            return SoundEvents.WOOD_HIT;
        }
        if (item == ModItems.BOTTLE.get() || (ours && path.contains("bottle"))) {
            return SoundEvents.GLASS_HIT;
        }
        if (!ours || item instanceof TaserItem || item instanceof WoodShieldItem) {
            return null;
        }
        return SoundEvents.METAL_HIT;
    }
}
