package tfar.gulliversblocks.duck;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tfar.gulliversblocks.MountPosition;

import java.util.Map;

public interface PlayerDuck {
    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }


}
