package tfar.gulliversblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.MountPosition;

public class C2SSwapHandsPacket implements C2SModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSwapHandsPacket> STREAM_CODEC =
            ModPacket.streamCodec(C2SSwapHandsPacket::new);


    public static final CustomPacketPayload.Type<C2SSwapHandsPacket> TYPE = ModPacket.type(C2SSwapHandsPacket.class);

    public C2SSwapHandsPacket() {

    }

    public C2SSwapHandsPacket(RegistryFriendlyByteBuf buf) {

    }

    @Override
    public void handleServer(ServerPlayer player) {
        GulliversBlocks.swap(player,MountPosition.LEFT_HAND,MountPosition.RIGHT_HAND);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {

    }
}
