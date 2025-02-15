package tfar.gulliversblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.duck.LivingEntityDuck;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.network.client.S2CSetMountPositionPacket;

import java.util.Map;

public class ClientPacketHandler {


    public static void setMountPos(S2CSetMountPositionPacket s2CSetMountPositionPacket) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level != null) {
            Entity entity = level.getEntity(s2CSetMountPositionPacket.entityId);
            if (entity instanceof LivingEntity livingEntity) {
                Entity passenger = level.getEntity(s2CSetMountPositionPacket.passengerId);
                    LivingEntityDuck playerDuck = LivingEntityDuck.of(livingEntity);
                    Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
                    mounts.put(s2CSetMountPositionPacket.mountPosition, passenger);
            }
        }
    }

    public static void removeMountPos(S2CRemoveMountPositionPacket s2CSetMountPositionPacket) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level != null) {
            Entity entity = level.getEntity(s2CSetMountPositionPacket.entityId);
            if (entity instanceof LivingEntity livingEntity) {
                LivingEntityDuck playerDuck = LivingEntityDuck.of(livingEntity);
                Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
                mounts.remove(s2CSetMountPositionPacket.mountPosition);
            }
        }
    }
}
