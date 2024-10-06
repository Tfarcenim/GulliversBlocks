package tfar.gulliversblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.PlayerDuck;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.network.client.S2CSetMountPositionPacket;
import tfar.gulliversblocks.platform.Services;

import java.util.Map;

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
        PlayerDuck playerDuck = PlayerDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();

        Entity mountRightHand = mounts.get(MountPosition.RIGHT_HAND);
        Entity mountLeftHand = mounts.get(MountPosition.LEFT_HAND);

        if (mountLeftHand != null) {
            mounts.put(MountPosition.RIGHT_HAND, mountLeftHand);
            Services.PLATFORM.sendToClient(new S2CSetMountPositionPacket(MountPosition.RIGHT_HAND,mountLeftHand),player);
        } else {
            mounts.remove(MountPosition.RIGHT_HAND);
            Services.PLATFORM.sendToClient(new S2CRemoveMountPositionPacket(MountPosition.RIGHT_HAND),player);
        }

        if (mountRightHand != null) {
            mounts.put(MountPosition.LEFT_HAND, mountRightHand);
            Services.PLATFORM.sendToClient(new S2CSetMountPositionPacket(MountPosition.LEFT_HAND,mountRightHand),player);
        } else {
            mounts.remove(MountPosition.LEFT_HAND);
            Services.PLATFORM.sendToClient(new S2CRemoveMountPositionPacket(MountPosition.LEFT_HAND),player);
        }

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {

    }
}
