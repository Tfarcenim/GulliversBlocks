package tfar.gulliversblocks.duck;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import tfar.gulliversblocks.GulliverScales;
import tfar.gulliversblocks.MountPosition;

import java.util.Map;

public interface LivingEntityDuck {

    Map<MountPosition, Entity> getMountPositions();

    void addMount(MountPosition position,Entity entity);
    void removeMount(MountPosition position);

    int gulliversBlocks$getGulliverScale();
    void gulliversBlocks$setGulliverScale(int gulliverScale);

    default void addGulliverScale(int scale) {
        int newScale = GulliverScales.clamp(scale + gulliversBlocks$getGulliverScale());
            gulliversBlocks$setGulliverScale(newScale);
    }

    static LivingEntityDuck of(LivingEntity living) {
        return (LivingEntityDuck) living;
    }
}
