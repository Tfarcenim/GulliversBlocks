package tfar.gulliversblocks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public interface PlayerDuck {
    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }

    Map<MountPosition, Entity> getMountPositions();
}
