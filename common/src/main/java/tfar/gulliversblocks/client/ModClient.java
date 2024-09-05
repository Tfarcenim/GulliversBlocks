package tfar.gulliversblocks.client;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tfar.gulliversblocks.network.C2SDropHeldEntityPacket;

import java.util.List;

public class ModClient {

    public static void onRightClickEmpty(Player player, InteractionHand hand) {
        List<Entity> passengers = player.getPassengers();
        if (!passengers.isEmpty()) {
            Entity passenger = passengers.getFirst();
            passenger.stopRiding();
            C2SDropHeldEntityPacket.send();
        }
    }

}
