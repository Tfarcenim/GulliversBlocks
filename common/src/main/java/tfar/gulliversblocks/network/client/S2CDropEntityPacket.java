package tfar.gulliversblocks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.client.ClientPacketHandler;
import tfar.gulliversblocks.network.ModPacket;
import tfar.gulliversblocks.network.S2CModPacket;
import tfar.gulliversblocks.platform.Services;

public class S2CDropEntityPacket implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CDropEntityPacket> STREAM_CODEC =
            ModPacket.streamCodec(S2CDropEntityPacket::new);


    public static final Type<S2CDropEntityPacket> TYPE = ModPacket.type(S2CDropEntityPacket.class);

    public final MountPosition mountPosition;

    public S2CDropEntityPacket(MountPosition mountPosition) {
        this.mountPosition = mountPosition;
    }

    public S2CDropEntityPacket(FriendlyByteBuf buf) {
        mountPosition = buf.readEnum(MountPosition.class);
    }

    public static void sendTo(ServerPlayer player, MountPosition mountPosition) {
        S2CDropEntityPacket packet = new S2CDropEntityPacket(mountPosition);
        Services.PLATFORM.sendToClient(packet,player);
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.dropEntity(this);
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
