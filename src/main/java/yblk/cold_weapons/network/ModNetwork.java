package yblk.cold_weapons.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import yblk.cold_weapons.ColdWeapons;

import java.util.function.Supplier;

public final class ModNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ColdWeapons.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals);
    private static int nextId;

    private ModNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(nextId++, TaserBeamPacket.class,
                TaserBeamPacket::encode, TaserBeamPacket::decode, TaserBeamPacket::handle);
    }

    public static final class TaserBeamPacket {
        public final int shooterId;
        public final double x;
        public final double y;
        public final double z;
        public final int targetId;
        public final boolean mainHand;

        public TaserBeamPacket(int shooterId, double x, double y, double z, int targetId, boolean mainHand) {
            this.shooterId = shooterId;
            this.x = x;
            this.y = y;
            this.z = z;
            this.targetId = targetId;
            this.mainHand = mainHand;
        }

        public static void encode(TaserBeamPacket msg, FriendlyByteBuf buf) {
            buf.writeVarInt(msg.shooterId);
            buf.writeDouble(msg.x);
            buf.writeDouble(msg.y);
            buf.writeDouble(msg.z);
            buf.writeVarInt(msg.targetId);
            buf.writeBoolean(msg.mainHand);
        }

        public static TaserBeamPacket decode(FriendlyByteBuf buf) {
            return new TaserBeamPacket(buf.readVarInt(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readVarInt(), buf.readBoolean());
        }

        public static void handle(TaserBeamPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                try {
                    Class.forName("yblk.cold_weapons.client.TaserBeams")
                            .getMethod("add", int.class, double.class, double.class, double.class, int.class, boolean.class)
                            .invoke(null, msg.shooterId, msg.x, msg.y, msg.z, msg.targetId, msg.mainHand);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException(e);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
