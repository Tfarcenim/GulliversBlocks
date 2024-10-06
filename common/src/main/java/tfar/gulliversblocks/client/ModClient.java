package tfar.gulliversblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.PlayerDuck;
import tfar.gulliversblocks.network.C2SDropHeldEntityPacket;
import tfar.gulliversblocks.network.C2SSwapHandsPacket;
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

    public static boolean onArmRender(Player player, HumanoidArm arm) {
        List<Entity> passengers = player.getPassengers();
        if (!passengers.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void interceptKeybinds(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
            if (!mounts.isEmpty()) {
                while (minecraft.options.keySwapOffhand.consumeClick()) {
                    if (!player.isSpectator()) {
                        Services.PLATFORM.sendToServer(new C2SSwapHandsPacket());
                    }
                }
            }
        }
    }
}
