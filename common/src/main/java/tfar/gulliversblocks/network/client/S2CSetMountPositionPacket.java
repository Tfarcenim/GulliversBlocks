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

public class S2CSetMountPositionPacket implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSetMountPositionPacket> STREAM_CODEC =
            ModPacket.streamCodec(S2CSetMountPositionPacket::new);


    public static final CustomPacketPayload.Type<S2CSetMountPositionPacket> TYPE = ModPacket.type(S2CSetMountPositionPacket.class);

    public final int entityId;
    public final MountPosition mountPosition;
    public final int passengerId;

    public S2CSetMountPositionPacket(Entity entity, MountPosition mountPosition, Entity passenger) {
        this.mountPosition = mountPosition;
        this.entityId = entity.getId();
        this.passengerId = passenger.getId();
    }

    public S2CSetMountPositionPacket(FriendlyByteBuf buf) {
        entityId = buf.readInt();
        mountPosition = buf.readEnum(MountPosition.class);
        passengerId = buf.readInt();
    }

    public static void sendToTracking(Entity entity,MountPosition mountPosition,Entity passenger) {
        S2CSetMountPositionPacket s2CSetMountPositionPacket = new S2CSetMountPositionPacket(entity,mountPosition,passenger);
        Services.PLATFORM.sendToTracking(s2CSetMountPositionPacket,entity);
        if (entity instanceof ServerPlayer serverPlayer) {
            Services.PLATFORM.sendToClient(s2CSetMountPositionPacket,serverPlayer);
        }
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.setMountPos(this);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeEnum(mountPosition);
        buf.writeInt(passengerId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
