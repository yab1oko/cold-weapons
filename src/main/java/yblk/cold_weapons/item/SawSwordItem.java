package yblk.cold_weapons.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import yblk.cold_weapons.config.ColdWeaponsConfig;

import java.util.List;

/** Chainsaw sword. RMB toggles the blade: full damage and the animated texture while charge lasts. */
public class SawSwordItem extends ChargedToolItem {
    /** Idle hit, with the blade off. */
    public static final float IDLE_DAMAGE = 4.0F;
    /** Current chainsaw damage, used only while the blade is running. */
    public static final float POWERED_DAMAGE = 8.0F;
    public static final float ATTACK_SPEED = 1.6F;

    public SawSwordItem(Tier tier, Properties properties) {
        super(tier, IDLE_DAMAGE, ATTACK_SPEED, properties);
    }

    @Override
    protected boolean drainsWhileCarried() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean turnOn = !ToolCharge.isRunning(stack);
        if (turnOn && ToolCharge.get(stack) <= 0.0D) {
            return InteractionResultHolder.fail(stack);
        }
        ToolCharge.setRunning(stack, turnOn);
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) {
            return super.getDefaultAttributeModifiers(slot);
        }
        float damage = ToolCharge.isRunning(stack)
                ? ColdWeaponsConfig.sawswordPoweredDamage()
                : ColdWeaponsConfig.sawswordIdleDamage();
        return modifiers(damage, ATTACK_SPEED);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tip, flag);
        tip.add(Component.translatable("item.cold_weapons.sawsword.use").withStyle(net.minecraft.ChatFormatting.GRAY));
    }
}
