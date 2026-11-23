package yblk.cold_weapons.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import yblk.cold_weapons.config.ColdWeaponsConfig;

/** Separate energy pool for the circular saw and chainsaw sword. Not durability. */
public final class ToolCharge {

    private static final String CHARGE = "Charge";
    private static final String RUNNING = "Running";

    private ToolCharge() {
    }

    public static double get(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(CHARGE)) {
            return ColdWeaponsConfig.chargeMax();
        }
        return Mth.clamp(tag.getDouble(CHARGE), 0.0D, ColdWeaponsConfig.chargeMax());
    }

    public static void set(ItemStack stack, double value) {
        stack.getOrCreateTag().putDouble(CHARGE, Mth.clamp(value, 0.0D, ColdWeaponsConfig.chargeMax()));
    }

    public static boolean isRunning(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean(RUNNING);
    }

    public static void setRunning(ItemStack stack, boolean running) {
        if (isRunning(stack) == running) {
            return;
        }
        stack.getOrCreateTag().putBoolean(RUNNING, running);
    }

    /** @return false when the pool hits zero */
    public static boolean drain(ItemStack stack, double amount) {
        double next = get(stack) - amount;
        if (next <= 0.0D) {
            set(stack, 0.0D);
            return false;
        }
        set(stack, next);
        return true;
    }

    /** @return false when the tool is already full */
    public static boolean addBattery(ItemStack stack) {
        double current = get(stack);
        if (current >= ColdWeaponsConfig.chargeMax() - 0.001D) {
            return false;
        }
        set(stack, Math.min(ColdWeaponsConfig.chargeMax(), current + ColdWeaponsConfig.batteryRestore()));
        return true;
    }
}
