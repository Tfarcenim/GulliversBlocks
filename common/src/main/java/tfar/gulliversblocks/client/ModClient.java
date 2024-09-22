package tfar.gulliversblocks.client;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import tfar.gulliversblocks.network.C2SDropHeldEntityPacket;

import java.util.List;

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
}
