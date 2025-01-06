package tfar.gulliversblocks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.client.ClientPacketHandler;
import tfar.gulliversblocks.network.ModPacket;
import tfar.gulliversblocks.network.S2CModPacket;
import tfar.gulliversblocks.platform.Services;

public class S2CRemoveMountPositionPacket implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CRemoveMountPositionPacket> STREAM_CODEC =
            ModPacket.streamCodec(S2CRemoveMountPositionPacket::new);


    public static final Type<S2CRemoveMountPositionPacket> TYPE = ModPacket.type(S2CRemoveMountPositionPacket.class);

    public final int entityId;
    public final MountPosition mountPosition;

    public S2CRemoveMountPositionPacket(Entity entity,MountPosition mountPosition) {
        entityId = entity.getId();
        this.mountPosition = mountPosition;
    }

    public S2CRemoveMountPositionPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        mountPosition = buf.readEnum(MountPosition.class);
    }

    public static void sendToTracking(Entity entity, MountPosition mountPosition) {
        S2CRemoveMountPositionPacket packet = new S2CRemoveMountPositionPacket(entity,mountPosition);
        Services.PLATFORM.sendToTracking(packet,entity);
        if (entity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendToClient(packet,serverPlayer);
        }
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.removeMountPos(this);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeEnum(mountPosition);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
