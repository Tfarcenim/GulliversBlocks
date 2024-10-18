package tfar.gulliversblocks.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.PlayerDuck;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.platform.Services;

import java.util.Map;

public class C2SActionPacket implements C2SModPacket<RegistryFriendlyByteBuf>  {

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SActionPacket> STREAM_CODEC =
            ModPacket.streamCodec(C2SActionPacket::new);

    public static final CustomPacketPayload.Type<C2SActionPacket> TYPE = ModPacket.type(C2SActionPacket.class);

    public final Action action;

    public C2SActionPacket(Action action) {
        this.action = action;
    }

    public C2SActionPacket(RegistryFriendlyByteBuf buf) {
        action = buf.readEnum(Action.class);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
        switch (action) {
            case THROW -> {
                HumanoidArm arm = player.getMainArm();
                switch (arm) {
                    case RIGHT -> {
                        Entity entity = mounts.get(MountPosition.RIGHT_HAND);
                        if (entity!= null) {
                            entity.stopRiding();
                            entity.hurtMarked = true;
                            entity.addDeltaMovement(player.getLookAngle().scale(2));
                            player.connection.send(new ClientboundSetPassengersPacket(player));
                            playerDuck.getMountPositions().remove(MountPosition.RIGHT_HAND);
                            S2CRemoveMountPositionPacket.send(MountPosition.RIGHT_HAND,player);
                        }
                    }
                }
            }
            case SWAP_HANDS -> GulliversBlocks.swap(player,MountPosition.LEFT_HAND,MountPosition.RIGHT_HAND);
            case SWAP_SHOULDER -> {
                GulliversBlocks.swap(player,MountPosition.RIGHT_HAND,MountPosition.RIGHT_SHOULDER);
                GulliversBlocks.swap(player,MountPosition.LEFT_HAND,MountPosition.LEFT_SHOULDER);
            }
        }
    }

    @Override
    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeEnum(action);
    }

    public static void send(Action action) {
        Services.PLATFORM.sendToServer(new C2SActionPacket(action));
    }

    public enum Action {
        THROW,SWAP_HANDS,SWAP_SHOULDER;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
