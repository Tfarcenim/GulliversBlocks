package tfar.gulliversblocks;

import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityDuck {

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
