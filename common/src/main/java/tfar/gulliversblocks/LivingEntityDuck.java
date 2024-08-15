package tfar.gulliversblocks;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityDuck {

    int gulliversBlocks$getGulliverScale();
    void gulliversBlocks$setGulliverScale(int gulliverScale);

    default void addGulliverScale(int scale) {
        gulliversBlocks$setGulliverScale(Mth.clamp(gulliversBlocks$getGulliverScale() + scale,GulliverScales.min(),GulliverScales.max()));
    }

    static LivingEntityDuck of(LivingEntity living) {
        return (LivingEntityDuck) living;
    }
}
