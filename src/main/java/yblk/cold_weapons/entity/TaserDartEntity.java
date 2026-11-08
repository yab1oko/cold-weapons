package yblk.cold_weapons.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import yblk.cold_weapons.effect.TasedEffect;
import yblk.cold_weapons.item.TaserItem;
import yblk.cold_weapons.registry.ModEffects;
import yblk.cold_weapons.registry.ModEntities;
import net.minecraft.world.effect.MobEffectInstance;

/** Electrode that flies out of the taser, then sticks so the wires have something to hang from. */
public class TaserDartEntity extends ThrowableProjectile {
    private static final EntityDataAccessor<Integer> STUCK_ID =
            SynchedEntityData.defineId(TaserDartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> MAIN_HAND =
            SynchedEntityData.defineId(TaserDartEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> LOCAL_X =
            SynchedEntityData.defineId(TaserDartEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> LOCAL_Y =
            SynchedEntityData.defineId(TaserDartEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> LOCAL_Z =
            SynchedEntityData.defineId(TaserDartEntity.class, EntityDataSerializers.FLOAT);

    private int blockStuckTicks;

    public TaserDartEntity(EntityType<? extends TaserDartEntity> type, Level level) {
        super(type, level);
        this.noPhysics = false;
    }

    public TaserDartEntity(Level level, LivingEntity shooter, boolean mainHand) {
        super(ModEntities.TASER_DART.get(), shooter, level);
        this.entityData.set(MAIN_HAND, mainHand);
    }

    public boolean isMainHand() {
        return this.entityData.get(MAIN_HAND);
    }

    public boolean isStuckToEntity() {
        return this.entityData.get(STUCK_ID) >= 0;
    }

    public int getStuckId() {
        return this.entityData.get(STUCK_ID);
    }

    public boolean isStuckToBlock() {
        return this.entityData.get(STUCK_ID) == -2;
    }

    public boolean isStuck() {
        return this.isStuckToEntity() || this.isStuckToBlock();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STUCK_ID, -1);
        this.entityData.define(MAIN_HAND, true);
        this.entityData.define(LOCAL_X, 0.0F);
        this.entityData.define(LOCAL_Y, 0.0F);
        this.entityData.define(LOCAL_Z, 0.0F);
    }

    @Override
    protected float getGravity() {
        return this.isStuck() ? 0.0F : 0.015F;
    }

    @Override
    public void tick() {
        if (this.isStuckToEntity()) {
            Entity stuck = this.level().getEntity(this.entityData.get(STUCK_ID));
            if (!(stuck instanceof LivingEntity living) || !living.isAlive() || !TasedEffect.isTased(living)) {
                if (!this.level().isClientSide) {
                    this.discard();
                }
                return;
            }
            Entity owner = this.getOwner();
            if (!this.level().isClientSide && owner != null && owner.distanceTo(living) > TaserItem.RANGE + 1.5D) {
                this.discard();
                return;
            }
            this.setPos(anchor(living));
            this.setDeltaMovement(Vec3.ZERO);
            return;
        }
        if (this.isStuckToBlock()) {
            this.setDeltaMovement(Vec3.ZERO);
            if (!this.level().isClientSide && ++this.blockStuckTicks > 18) {
                this.discard();
            }
            return;
        }
        super.tick();
        if (!this.level().isClientSide && this.tickCount > 24) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.isStuck()) {
            return;
        }
        Entity target = result.getEntity();
        if (target == this.getOwner() || !(target instanceof LivingEntity living) || !living.isAlive()) {
            return;
        }
        if (!this.level().isClientSide) {
            living.addEffect(new MobEffectInstance(ModEffects.TASED.get(), TasedEffect.DURATION_TICKS, 0, false, false, true));
            stickTo(living, result.getLocation());
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (this.isStuck()) {
            return;
        }
        this.setPos(result.getLocation());
        this.setDeltaMovement(Vec3.ZERO);
        this.noPhysics = true;
        this.entityData.set(STUCK_ID, -2);
        this.blockStuckTicks = 1;
    }

    private void stickTo(LivingEntity living, Vec3 worldHit) {
        Vec3 delta = worldHit.subtract(living.position());
        float yaw = living.getYRot() * Mth.DEG_TO_RAD;
        double cos = Mth.cos(yaw);
        double sin = Mth.sin(yaw);
        this.entityData.set(LOCAL_X, (float) (delta.x * cos + delta.z * sin));
        this.entityData.set(LOCAL_Y, (float) delta.y);
        this.entityData.set(LOCAL_Z, (float) (-delta.x * sin + delta.z * cos));
        this.entityData.set(STUCK_ID, living.getId());
        this.setDeltaMovement(Vec3.ZERO);
        this.noPhysics = true;
        this.setPos(worldHit);
    }

    private Vec3 anchor(LivingEntity living) {
        float yaw = living.getYRot() * Mth.DEG_TO_RAD;
        double cos = Mth.cos(yaw);
        double sin = Mth.sin(yaw);
        double localX = this.entityData.get(LOCAL_X);
        double localZ = this.entityData.get(LOCAL_Z);
        double worldX = localX * cos - localZ * sin;
        double worldZ = localX * sin + localZ * cos;
        return living.position().add(worldX, this.entityData.get(LOCAL_Y), worldZ);
    }
}
