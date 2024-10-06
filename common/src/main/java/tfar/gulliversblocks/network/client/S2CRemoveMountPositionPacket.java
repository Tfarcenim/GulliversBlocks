package tfar.gulliversblocks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.client.ClientPacketHandler;
import tfar.gulliversblocks.network.ModPacket;
import tfar.gulliversblocks.network.S2CModPacket;

public class S2CRemoveMountPositionPacket implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CRemoveMountPositionPacket> STREAM_CODEC =
            ModPacket.streamCodec(S2CRemoveMountPositionPacket::new);


    public static final Type<S2CRemoveMountPositionPacket> TYPE = ModPacket.type(S2CRemoveMountPositionPacket.class);
    public final MountPosition mountPosition;

    public S2CRemoveMountPositionPacket(MountPosition mountPosition) {
        this.mountPosition = mountPosition;
    }

    public S2CRemoveMountPositionPacket(FriendlyByteBuf buf) {
        mountPosition = buf.readEnum(MountPosition.class);
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.removeMountPos(this);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(mountPosition);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
