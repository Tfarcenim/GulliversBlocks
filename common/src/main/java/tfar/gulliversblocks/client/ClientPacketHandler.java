package tfar.gulliversblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.PlayerDuck;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.network.client.S2CSetMountPositionPacket;

import java.util.Map;

public class ClientPacketHandler {


    public static void setMountPos(S2CSetMountPositionPacket s2CSetMountPositionPacket) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level != null) {
            Entity entity = level.getEntity(s2CSetMountPositionPacket.entityId);
            if (entity != null) {
                Player player = mc.player;
                if (player != null) {
                    PlayerDuck playerDuck = PlayerDuck.of(player);
                    Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
                    mounts.put(s2CSetMountPositionPacket.mountPosition, entity);
                }
            }
        }
    }

    public static void removeMountPos(S2CRemoveMountPositionPacket s2CSetMountPositionPacket) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level != null) {
            Player player = mc.player;
            if (player != null) {
                PlayerDuck playerDuck = PlayerDuck.of(player);
                Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
                mounts.remove(s2CSetMountPositionPacket.mountPosition);
            }
        }
    }
}
