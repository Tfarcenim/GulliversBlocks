package tfar.gulliversblocks.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.client.ClientPacketHandler;
import tfar.gulliversblocks.network.ModPacket;
import tfar.gulliversblocks.network.S2CModPacket;

public class S2CSetMountPositionPacket implements S2CModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSetMountPositionPacket> STREAM_CODEC =
            ModPacket.streamCodec(S2CSetMountPositionPacket::new);


    public static final CustomPacketPayload.Type<S2CSetMountPositionPacket> TYPE = ModPacket.type(S2CSetMountPositionPacket.class);
    public final MountPosition mountPosition;
    public final int entityId;

    public S2CSetMountPositionPacket(MountPosition mountPosition, Entity entity) {
        this.mountPosition = mountPosition;
        this.entityId = entity.getId();
    }

    public S2CSetMountPositionPacket(FriendlyByteBuf buf) {
        mountPosition = buf.readEnum(MountPosition.class);
        entityId = buf.readInt();
    }

    @Override
    public void handleClient() {
        ClientPacketHandler.setMountPos(this);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(mountPosition);
        buf.writeInt(entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
