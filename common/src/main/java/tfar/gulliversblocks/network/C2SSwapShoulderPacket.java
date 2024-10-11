package tfar.gulliversblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.platform.Services;

public class C2SSwapShoulderPacket implements C2SModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSwapShoulderPacket> STREAM_CODEC =
            ModPacket.streamCodec(C2SSwapShoulderPacket::new);


    public static final Type<C2SSwapShoulderPacket> TYPE = ModPacket.type(C2SSwapShoulderPacket.class);

    public C2SSwapShoulderPacket() {

    }

    public C2SSwapShoulderPacket(RegistryFriendlyByteBuf buf) {

    }

    public static void send() {
        Services.PLATFORM.sendToServer(new C2SSwapShoulderPacket());
    }

    @Override
    public void handleServer(ServerPlayer player) {
        GulliversBlocks.swap(player,MountPosition.RIGHT_HAND,MountPosition.RIGHT_SHOULDER);
        GulliversBlocks.swap(player,MountPosition.LEFT_HAND,MountPosition.LEFT_SHOULDER);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {

    }
}
