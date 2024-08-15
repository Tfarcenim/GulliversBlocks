package tfar.gulliversblocks.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.LivingEntityDuck;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityDuck {
    @Unique
    int gulliversBlocks$gulliverScale;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public int gulliversBlocks$getGulliverScale() {
        return gulliversBlocks$gulliverScale;
    }

    @Override
    public void gulliversBlocks$setGulliverScale(int gulliverScale) {
        if (!this.level().isClientSide) {
            GulliversBlocks.onGulliverScaleChange((LivingEntity) (Object)this,gulliversBlocks$gulliverScale,gulliverScale);
        }
        this.gulliversBlocks$gulliverScale = gulliverScale;

    }

    @Inject(method = "readAdditionalSaveData",at = @At("HEAD"))
    private void read(CompoundTag pCompound, CallbackInfo ci) {
        gulliversBlocks$gulliverScale = pCompound.getInt("gulliver_scale");
    }

    @Inject(method = "addAdditionalSaveData",at = @At("HEAD"))
    private void save(CompoundTag pCompound, CallbackInfo ci){
        pCompound.putInt("gulliver_scale",gulliversBlocks$gulliverScale);
    }
}