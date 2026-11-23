package yblk.cold_weapons.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import yblk.cold_weapons.config.ColdWeaponsConfig;

/** Optional junk drop. Does nothing while the config flag is off. */
public class ZombieWeaponModifier extends LootModifier {
    public static final Codec<ZombieWeaponModifier> CODEC = RecordCodecBuilder.create(instance ->
            codecStart(instance).apply(instance, ZombieWeaponModifier::new));

    public ZombieWeaponModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generated, LootContext context) {
        if (!ColdWeaponsConfig.zombieDropsEnabled()) {
            return generated;
        }
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity == null || !listed(ColdWeaponsConfig.zombieDropEntities(), ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()))) {
            return generated;
        }
        if (context.getRandom().nextDouble() >= ColdWeaponsConfig.zombieDropChance()) {
            return generated;
        }
        Item item = pick(context);
        if (item != null && item != Items.AIR) {
            generated.add(new ItemStack(item));
        }
        return generated;
    }

    private static Item pick(LootContext context) {
        var ids = ColdWeaponsConfig.zombieDropItems();
        if (ids.isEmpty()) {
            return null;
        }
        String raw = ids.get(context.getRandom().nextInt(ids.size()));
        ResourceLocation id = parse(raw);
        return id == null ? null : ForgeRegistries.ITEMS.getValue(id);
    }

    private static boolean listed(java.util.List<? extends String> ids, ResourceLocation key) {
        if (key == null) {
            return false;
        }
        for (String raw : ids) {
            ResourceLocation id = parse(raw);
            if (key.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private static ResourceLocation parse(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String text = raw.trim();
        return ResourceLocation.tryParse(text.contains(":") ? text : "minecraft:" + text);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
