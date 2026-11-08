package yblk.cold_weapons.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yblk.cold_weapons.ColdWeapons;
import yblk.cold_weapons.item.MechSawItem;
import yblk.cold_weapons.item.SawSwordItem;
import yblk.cold_weapons.item.ToolCharge;
import yblk.cold_weapons.registry.ModSounds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Start plays on the click that turns the tool on. Idle loops while it runs.
 * Both are cut the moment the tool stops. The circular saw then plays its end sound,
 * which is also cut if the saw starts again before that clip finishes.
 */
@Mod.EventBusSubscriber(modid = ColdWeapons.MODID, value = Dist.CLIENT)
public final class ToolUseSounds {
    private static final Map<Integer, Playback> ACTIVE = new HashMap<>();

    private ToolUseSounds() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            stopAll();
            return;
        }
        for (Player player : minecraft.level.players()) {
            Playback playback = ACTIVE.computeIfAbsent(player.getId(), id -> new Playback());
            playback.update(player, modeOf(player));
        }
        Iterator<Map.Entry<Integer, Playback>> it = ACTIVE.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Playback> entry = it.next();
            if (minecraft.level.getEntity(entry.getKey()) instanceof Player) {
                continue;
            }
            entry.getValue().silence();
            it.remove();
        }
    }

    private static Mode modeOf(Player player) {
        if (player.isUsingItem()) {
            ItemStack using = player.getUseItem();
            if (using.getItem() instanceof MechSawItem && ToolCharge.get(using) > 0.0D) {
                return Mode.CIRCULAR;
            }
        }
        if (runningSaw(player.getMainHandItem()) || runningSaw(player.getOffhandItem())) {
            return Mode.SAWSWORD;
        }
        return Mode.NONE;
    }

    private static boolean runningSaw(ItemStack stack) {
        return stack.getItem() instanceof SawSwordItem
                && ToolCharge.isRunning(stack)
                && ToolCharge.get(stack) > 0.0D;
    }

    private static void stopAll() {
        ACTIVE.values().forEach(Playback::silence);
        ACTIVE.clear();
    }

    private enum Mode {
        NONE, CIRCULAR, SAWSWORD
    }

    private static final class Playback {
        private Mode mode = Mode.NONE;
        private SoundInstance start;
        private LoopSound idle;
        private SoundInstance end;

        void update(Player player, Mode next) {
            if (next == mode) {
                return;
            }
            cut(start);
            start = null;
            if (idle != null) {
                idle.halt();
                cut(idle);
                idle = null;
            }
            if (mode == Mode.CIRCULAR) {
                end = play(player, ModSounds.CIRCULAR_END.get(), 2.0F);
            } else {
                cut(end);
                end = null;
            }
            mode = next;
            if (next == Mode.NONE) {
                return;
            }
            cut(end);
            end = null;
            SoundEvent startEvent = next == Mode.CIRCULAR
                    ? ModSounds.CIRCULAR_START.get()
                    : ModSounds.SAWSWORD_START.get();
            SoundEvent idleEvent = next == Mode.CIRCULAR
                    ? ModSounds.CIRCULAR_IDLE.get()
                    : ModSounds.SAWSWORD_IDLE.get();
            float startVolume = next == Mode.CIRCULAR ? 2.4F : 1.0F;
            float idleVolume = next == Mode.CIRCULAR ? 0.35F : 1.0F;
            start = play(player, startEvent, startVolume);
            idle = new LoopSound(player, idleEvent, idleVolume);
            Minecraft.getInstance().getSoundManager().play(idle);
        }

        void silence() {
            cut(start);
            start = null;
            if (idle != null) {
                idle.halt();
                cut(idle);
                idle = null;
            }
            cut(end);
            end = null;
            mode = Mode.NONE;
        }

        private static SoundInstance play(Player player, SoundEvent event, float volume) {
            boolean local = player == Minecraft.getInstance().player;
            SoundInstance instance = new SimpleSoundInstance(
                    event.getLocation(),
                    SoundSource.PLAYERS,
                    volume,
                    1.0F,
                    RandomSource.create(),
                    false,
                    0,
                    SoundInstance.Attenuation.NONE,
                    local ? 0.0D : player.getX(),
                    local ? 0.0D : player.getY(),
                    local ? 0.0D : player.getZ(),
                    local);
            Minecraft.getInstance().getSoundManager().play(instance);
            return instance;
        }

        private static void cut(SoundInstance instance) {
            if (instance != null) {
                Minecraft.getInstance().getSoundManager().stop(instance);
            }
        }
    }

    private static final class LoopSound extends AbstractTickableSoundInstance {
        private final Player player;

        private LoopSound(Player player, SoundEvent event, float volume) {
            super(event, SoundSource.PLAYERS, RandomSource.create());
            this.player = player;
            this.looping = true;
            this.delay = 0;
            this.volume = volume;
            this.pitch = 1.0F;
            this.relative = player == Minecraft.getInstance().player;
            this.x = this.relative ? 0.0D : player.getX();
            this.y = this.relative ? 0.0D : player.getY();
            this.z = this.relative ? 0.0D : player.getZ();
        }

        void halt() {
            stop();
        }

        @Override
        public void tick() {
            if (player.isRemoved()) {
                stop();
                return;
            }
            if (!this.relative) {
                this.x = player.getX();
                this.y = player.getY();
                this.z = player.getZ();
            }
        }
    }
}
