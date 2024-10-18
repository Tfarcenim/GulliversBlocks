package tfar.gulliversblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.PlayerDuck;
import tfar.gulliversblocks.network.C2SActionPacket;
import tfar.gulliversblocks.network.C2SDropHeldEntityPacket;
import tfar.gulliversblocks.platform.Services;

import java.util.List;
import java.util.Map;

public class ModClient {

    public static void onRightClickEmpty(Player player, InteractionHand hand) {
        boolean sneak = player.isCrouching();
        List<Entity> passengers = player.getPassengers();
        if (!passengers.isEmpty()) {
            if (sneak) {
                Entity passenger = passengers.getFirst();
                passenger.stopRiding();
                C2SDropHeldEntityPacket.send();
            }
        }
    }

    public static void onLeftClickEmpty(Player player) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
        HumanoidArm arm = player.getMainArm();
        switch (arm) {
            case RIGHT -> {
                Entity entity = mounts.get(MountPosition.RIGHT_HAND);
                if (entity != null) {
                    C2SActionPacket.send(C2SActionPacket.Action.THROW);
                }
            }
            case LEFT -> {
                Entity entity = mounts.get(MountPosition.LEFT_HAND);
                if (entity != null) {
                    C2SActionPacket.send(C2SActionPacket.Action.THROW);
                }
            }
        }
    }

    public static boolean onArmRender(Player player, HumanoidArm arm) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();

        if (arm == HumanoidArm.RIGHT) {
            return mounts.get(MountPosition.RIGHT_HAND) != null;
        } else {
            return mounts.get(MountPosition.LEFT_HAND) != null;
        }
    }

    public static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer pPlayer, InteractionHand pHand) {
        PlayerDuck playerDuck = PlayerDuck.of(pPlayer);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
        HumanoidArm arm = pPlayer.getMainArm();
        switch (pHand) {
            case MAIN_HAND -> {
                if (arm == HumanoidArm.RIGHT) {
                    return mounts.get(MountPosition.RIGHT_HAND) != null ? HumanoidModel.ArmPose.ITEM : null;
                } else {
                    return mounts.get(MountPosition.LEFT_HAND) != null ? HumanoidModel.ArmPose.ITEM : null;
                }
            }
            case OFF_HAND -> {
                if (arm == HumanoidArm.LEFT) {
                    return mounts.get(MountPosition.RIGHT_HAND) != null ? HumanoidModel.ArmPose.ITEM : null;
                } else {
                    return mounts.get(MountPosition.LEFT_HAND) != null ? HumanoidModel.ArmPose.ITEM : null;
                }
            }
        }
        return null;
    }

    public static void interceptKeybinds(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
            if (!mounts.isEmpty()) {
                while (minecraft.options.keySwapOffhand.consumeClick()) {
                    if (!player.isSpectator()) {
                        C2SActionPacket.send(C2SActionPacket.Action.SWAP_HANDS);
                    }
                }
            }
        }
    }

    public static void keyPressed(Minecraft client) {
        if (ModKeybinds.swap_shoulder.consumeClick()) {
            C2SActionPacket.send(C2SActionPacket.Action.SWAP_SHOULDER);
        }
    }
}
