package tfar.gulliversblocks.duck;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tfar.gulliversblocks.GulliverScales;
import tfar.gulliversblocks.MountPosition;

import java.util.Map;

public interface LivingEntityDuck {

    Map<MountPosition, Entity> getMountPositions();

    int gulliversBlocks$getGulliverScale();
    void gulliversBlocks$setGulliverScale(int gulliverScale);

    default void addGulliverScale(int scale) {
        if (GulliverScales.valid(gulliversBlocks$getGulliverScale() + scale)) {
            gulliversBlocks$setGulliverScale(gulliversBlocks$getGulliverScale() + scale);
        }
    }

    static LivingEntityDuck of(LivingEntity living) {
        return (LivingEntityDuck) living;
    }
}
