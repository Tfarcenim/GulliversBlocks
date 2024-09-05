package tfar.gulliversblocks.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;


public record S2CInitialEntityPacket(int entityID) implements S2CModPacket {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CInitialEntityPacket> STREAM_CODEC =
            StreamCodec.ofMember(S2CInitialEntityPacket::write, S2CInitialEntityPacket::new);


    public static final CustomPacketPayload.Type<S2CInitialEntityPacket> TYPE = new CustomPacketPayload.Type<>(
            PacketHandler.packet(S2CInitialEntityPacket.class));


    public S2CInitialEntityPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    @Override
    public void handleClient() {

    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}