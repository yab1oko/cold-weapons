package yblk.cold_weapons.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import yblk.cold_weapons.registry.ModEntities;
import yblk.cold_weapons.registry.ModItems;

public class ThrownKnifeEntity extends AbstractArrow {
    private static final EntityDataAccessor<ItemStack> DATA_KNIFE =
            SynchedEntityData.defineId(ThrownKnifeEntity.class, EntityDataSerializers.ITEM_STACK);

    private ItemStack knifeItem = ItemStack.EMPTY;
    private float throwDamage = 5.0F;
    private boolean dealtDamage;

    public ThrownKnifeEntity(EntityType<? extends ThrownKnifeEntity> type, Level level) {
        super(type, level);
    }

    public ThrownKnifeEntity(Level level, LivingEntity shooter, ItemStack stack, float damage) {
        super(ModEntities.THROWN_KNIFE.get(), shooter, level);
        this.setKnifeItem(stack.copyWithCount(1));
        this.throwDamage = damage;
        this.setBaseDamage(damage);
        this.pickup = AbstractArrow.Pickup.ALLOWED;
        this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_KNIFE, ItemStack.EMPTY);
    }

    public void setKnifeItem(ItemStack stack) {
        this.knifeItem = stack.copyWithCount(1);
        this.entityData.set(DATA_KNIFE, this.knifeItem.copy());
    }

    public ItemStack getKnifeItem() {
        ItemStack synced = this.entityData.get(DATA_KNIFE);
        return synced.isEmpty() ? new ItemStack(ModItems.KNIFE.get()) : synced;
    }

    public boolean isInGround() {
        return this.inGround;
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.knifeItem.isEmpty() ? this.getKnifeItem().copy() : this.knifeItem.copy();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        Entity owner = this.getOwner();
        if (this.dealtDamage || target == owner) {
            return;
        }

        float hitDamage = (float) this.getBaseDamage();
        if (hitDamage <= 0.0F) {
            hitDamage = this.throwDamage;
        }
        if (target instanceof LivingEntity living) {
            Vec3 hit = result.getLocation();
            double headThreshold = living.getY() + living.getBbHeight() * 0.85D;
            if (hit.y >= headThreshold) {
                hitDamage *= 2.0F;
            }
        }

        boolean hurt = target.hurt(this.damageSources().arrow(this, owner == null ? this : owner), hitDamage);
        if (hurt) {
            this.dealtDamage = true;
            this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
            if (!this.level().isClientSide) {
                if (this.applyDurabilityLoss(10)) {
                    this.spawnAtLocation(this.getPickupItem());
                }
                this.discard();
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide && !this.applyDurabilityLoss(1)) {
            this.discard();
            return;
        }
        super.onHitBlock(result);
        this.setSoundEvent(SoundEvents.TRIDENT_HIT_GROUND);
    }

    /**
     * @return false if the knife broke and should not remain
     */
    private boolean applyDurabilityLoss(int amount) {
        ItemStack stack = this.knifeItem.isEmpty() ? this.getKnifeItem().copy() : this.knifeItem.copy();
        if (stack.isDamageableItem() && !(this.getOwner() instanceof Player player && player.getAbilities().instabuild)) {
            int damage = stack.getDamageValue() + amount;
            if (damage >= stack.getMaxDamage()) {
                this.playSound(SoundEvents.ITEM_BREAK, 0.8F, 0.8F + this.random.nextFloat() * 0.4F);
                this.knifeItem = ItemStack.EMPTY;
                this.entityData.set(DATA_KNIFE, ItemStack.EMPTY);
                return false;
            }
            stack.setDamageValue(damage);
        }
        this.setKnifeItem(stack);
        return true;
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGroundTime > 1) {
            this.dealtDamage = true;
        }
        if (this.tickCount > 1200 && !this.level().isClientSide && !this.inGround) {
            if (this.applyDurabilityLoss(1)) {
                this.spawnAtLocation(this.getPickupItem());
            }
            this.discard();
        }
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level().isClientSide || !this.inGround && this.getOwner() != player) {
            return;
        }
        super.playerTouch(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!this.knifeItem.isEmpty()) {
            tag.put("KnifeItem", this.knifeItem.save(new CompoundTag()));
        }
        tag.putFloat("ThrowDamage", this.throwDamage);
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("KnifeItem", 10)) {
            this.setKnifeItem(ItemStack.of(tag.getCompound("KnifeItem")));
        }
        this.throwDamage = tag.contains("ThrowDamage") ? tag.getFloat("ThrowDamage") : 5.0F;
        this.dealtDamage = tag.getBoolean("DealtDamage");
    }
}
