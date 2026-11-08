package yblk.cold_weapons.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

/**
 * Five-second taser stun. Movement is cut to a crawl; attacks and item use are blocked by events.
 */
public class TasedEffect extends MobEffect {
    public static final int DURATION_TICKS = 5 * 20;

    public TasedEffect() {
        super(MobEffectCategory.HARMFUL, 0xFFE14A);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "6f0a1c2e-7b34-4d11-9a20-c1e5a5700001",
                -0.62D, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "6f0a1c2e-7b34-4d11-9a20-c1e5a5700002",
                -1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static boolean isTased(LivingEntity entity) {
        return entity != null && entity.hasEffect(yblk.cold_weapons.registry.ModEffects.TASED.get());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof net.minecraft.server.level.ServerLevel server)) {
            return;
        }
        double x = entity.getX();
        double y = entity.getY() + entity.getBbHeight() * 0.55D;
        double z = entity.getZ();
        var random = entity.getRandom();
        for (int i = 0; i < 5; i++) {
            double ox = (random.nextDouble() - 0.5D) * entity.getBbWidth();
            double oy = (random.nextDouble() - 0.5D) * entity.getBbHeight() * 0.8D;
            double oz = (random.nextDouble() - 0.5D) * entity.getBbWidth();
            server.sendParticles(net.minecraft.core.particles.ParticleTypes.ELECTRIC_SPARK,
                    x + ox, y + oy, z + oz,
                    0,
                    (random.nextDouble() - 0.5D) * 0.45D,
                    random.nextDouble() * 0.25D,
                    (random.nextDouble() - 0.5D) * 0.45D,
                    1.0D);
        }
        server.sendParticles(new net.minecraft.core.particles.DustParticleOptions(
                        new org.joml.Vector3f(0.55F, 0.85F, 1.0F), 0.7F),
                x, y, z, 2, 0.2D, 0.3D, 0.2D, 0.0D);
    }

    @Override
    public java.util.List<ItemStack> getCurativeItems() {
        return java.util.List.of();
    }
}
