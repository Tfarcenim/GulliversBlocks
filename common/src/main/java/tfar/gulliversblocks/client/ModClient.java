package tfar.gulliversblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.config.GulliversBlocksConfig;
import tfar.gulliversblocks.duck.LivingEntityDuck;
import tfar.gulliversblocks.network.C2SActionPacket;

import java.util.Map;

public class ModClient {

    public static void onRightClickEmpty(Player player, InteractionHand hand) {

    }

    public static void onLeftClickEmpty(Player player) {
        LivingEntityDuck playerDuck = LivingEntityDuck.of(player);
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
        LivingEntityDuck playerDuck = LivingEntityDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();

        if (arm == HumanoidArm.RIGHT) {
            return mounts.get(MountPosition.RIGHT_HAND) != null;
        } else {
            return mounts.get(MountPosition.LEFT_HAND) != null;
        }
    }

    public static HumanoidModel.ArmPose getArmPose(AbstractClientPlayer pPlayer, InteractionHand pHand) {
        LivingEntityDuck playerDuck = LivingEntityDuck.of(pPlayer);
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
        LocalPlayer player = minecraft.player;
        if (player != null) {
            LivingEntityDuck playerDuck = LivingEntityDuck.of(player);
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

    public static void adjustArms(Player player,ModelPart leftArm,ModelPart rightArm) {
        if (player.getBbHeight() < GulliversBlocksConfig.Server.PAPER_FLOAT_SIZE.get() && GulliversBlocks.eitherHandHas(player,stack -> stack.is(Items.PAPER))) {
            float x = (float) Math.PI;
            float z = 0.05f;

            changeRotation(rightArm, -x, 0, -z);
            changeRotation(leftArm, -x, 0, z);
        }
    }

    private static void changeRotation(ModelPart part, float x, float y, float z) {
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }

}
