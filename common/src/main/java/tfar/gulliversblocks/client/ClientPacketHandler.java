package tfar.gulliversblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.duck.LivingEntityDuck;
import tfar.gulliversblocks.network.client.S2CDropEntityPacket;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.network.client.S2CSetMountPositionPacket;

public class ClientPacketHandler {


    public static void setMountPos(S2CSetMountPositionPacket s2CSetMountPositionPacket) {
        Minecraft mc = Minecraft.getInstance();
        Level level = mc.level;
        if (level != null) {
            Entity entity = level.getEntity(s2CSetMountPositionPacket.entityId);
            if (entity instanceof LivingEntity livingEntity) {
                Entity passenger = level.getEntity(s2CSetMountPositionPacket.passengerId);
                LivingEntityDuck playerDuck = LivingEntityDuck.of(livingEntity);
                playerDuck.addMount(s2CSetMountPositionPacket.mountPosition, passenger);
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
                playerDuck.removeMount(s2CSetMountPositionPacket.mountPosition);
            }
        }
    }

    public static void dropEntity(S2CDropEntityPacket s2CSetMountPositionPacket) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player != null) {
            LivingEntityDuck duck = LivingEntityDuck.of(player);
            Entity entity = duck.getMountPositions().get(s2CSetMountPositionPacket.mountPosition);
            if (entity != null) {
                entity.stopRiding();
            } else {
                GulliversBlocks.LOG.warn("Tried to dismount a null entity");
            }
        }
    }
}
