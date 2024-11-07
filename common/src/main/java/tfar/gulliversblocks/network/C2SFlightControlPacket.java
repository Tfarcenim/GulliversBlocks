package tfar.gulliversblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Parrot;
import tfar.gulliversblocks.PlayerDuck;
import tfar.gulliversblocks.platform.Services;

public class C2SFlightControlPacket implements C2SModPacket<RegistryFriendlyByteBuf> {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SFlightControlPacket> STREAM_CODEC =
            ModPacket.streamCodec(C2SFlightControlPacket::new);


    public static final CustomPacketPayload.Type<C2SFlightControlPacket> TYPE = ModPacket.type(C2SFlightControlPacket.class);

    public final double forward;
    public final double strafe;
    public final double up;

    public C2SFlightControlPacket(RegistryFriendlyByteBuf buf) {
        forward = buf.readDouble();
        strafe = buf.readDouble();
        up = buf.readDouble();
    }

    public C2SFlightControlPacket(double forward, double strafe, double up) {
        this.forward = forward;
        this.strafe = strafe;
        this.up = up;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PlayerDuck.of(player).getFlightControls().set(forward,strafe,up);
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeDouble(forward);
        buf.writeDouble(strafe);
        buf.writeDouble(up);
    }

    public static void send(double forward,double strafe, double up) {
        Services.PLATFORM.sendToServer(new C2SFlightControlPacket(forward,strafe,up));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
