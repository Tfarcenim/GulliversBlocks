package tfar.gulliversblocks.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public class C2SSwapHandsPacket implements C2SModPacket{
    @Override
    public void handleServer(ServerPlayer player) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return null;
    }
}
