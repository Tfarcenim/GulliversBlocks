package tfar.gulliversblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import tfar.gulliversblocks.platform.Services;

import java.util.List;


public class C2SDropHeldEntityPacket implements C2SModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SDropHeldEntityPacket> STREAM_CODEC =
            ModPacket.streamCodec(C2SDropHeldEntityPacket::new);


    public static final CustomPacketPayload.Type<C2SDropHeldEntityPacket> TYPE = ModPacket.type(C2SDropHeldEntityPacket.class);

    public C2SDropHeldEntityPacket() {

    }

    public static void send() {
        Services.PLATFORM.sendToServer(new C2SDropHeldEntityPacket());
    }

    public C2SDropHeldEntityPacket(RegistryFriendlyByteBuf buf) {

    }

    @Override
    public void handleServer(ServerPlayer player) {
        List<Entity> passengers = player.getPassengers();
        if (!passengers.isEmpty()) {
            Entity passenger = passengers.getFirst();
            passenger.stopRiding();
        }
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
