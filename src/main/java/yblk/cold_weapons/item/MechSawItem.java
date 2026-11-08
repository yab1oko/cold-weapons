package yblk.cold_weapons.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

/** Circular saw: hold right-click to spin the blade, animate it, and spend charge. */
public class MechSawItem extends ChargedToolItem {
    private static final float SPIN_DAMAGE = 0.5F;
    private static final double REACH = 2.6D;
    private static final double HALF_WIDTH = 0.7D;

    public MechSawItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        // Not SPEAR — that is the trident wind-up. Pose is handled on the client.
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (ToolCharge.get(stack) <= 0.0D) {
            return InteractionResultHolder.fail(stack);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        // Loaded reflectively so dedicated servers never touch client-only classes.
        try {
            Class<?> clazz = Class.forName("yblk.cold_weapons.client.MechSawClientExtensions");
            consumer.accept((IClientItemExtensions) clazz.getField("INSTANCE").get(null));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to load mech saw client extensions", e);
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseDuration) {
        if (level.isClientSide || !(user instanceof Player player) || !(level instanceof ServerLevel server)) {
            return;
        }
        if (stack.getDamageValue() >= stack.getMaxDamage() - 1 || ToolCharge.get(stack) <= 0.0D) {
            player.stopUsingItem();
            return;
        }
        int used = getUseDuration(stack) - remainingUseDuration;
        if (used > 0 && used % 20 == 0) {
            boolean hasCharge = ToolCharge.drain(stack, ToolCharge.PER_SECOND);
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.inventoryMenu.broadcastChanges();
            }
            if (!hasCharge) {
                player.stopUsingItem();
                return;
            }
        }

        Vec3 look = player.getLookAngle();
        Vec3 eye = player.getEyePosition();
        Vec3 tip = eye.add(look.scale(REACH));
        AABB box = new AABB(eye, tip).inflate(HALF_WIDTH);

        boolean hit = false;
        for (Entity entity : level.getEntities(player, box, e -> e instanceof LivingEntity living
                && living.isAlive()
                && !living.isSpectator()
                && player.canAttack(living))) {
            LivingEntity living = (LivingEntity) entity;
            living.invulnerableTime = 0;
            // Keep almost all pre-hit velocity so spin DPS does not shove mobs away.
            Vec3 motion = living.getDeltaMovement();
            if (living.hurt(level.damageSources().playerAttack(player), SPIN_DAMAGE)) {
                living.setDeltaMovement(
                        motion.x * 0.85D + living.getDeltaMovement().x * 0.15D,
                        motion.y,
                        motion.z * 0.85D + living.getDeltaMovement().z * 0.15D);
                living.hurtMarked = true;
                hit = true;
                spawnHitEffects(server, living);
            }
        }

        if (hit && player.tickCount % 4 == 0) {
            stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

    private static void spawnHitEffects(ServerLevel level, LivingEntity target) {
        double x = target.getX();
        double y = target.getY() + target.getBbHeight() * 0.55D;
        double z = target.getZ();

        level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.08D);
        level.sendParticles(ParticleTypes.CRIT, x, y, z, 4, 0.2D, 0.2D, 0.2D, 0.15D);

        if (target.tickCount % 2 == 0) {
            level.playSound(null, x, y, z, SoundEvents.GRINDSTONE_USE, SoundSource.PLAYERS,
                    0.35F, 1.35F + target.getRandom().nextFloat() * 0.25F);
        }
    }
}
