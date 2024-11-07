package tfar.gulliversblocks.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.GulliversBlocks;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity{
    protected MobMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "getControllingPassenger",at = @At(value = "TAIL"),cancellable = true)
    private void modifyControllingPassenger(CallbackInfoReturnable<LivingEntity> cir) {
        LivingEntity living = GulliversBlocks.forcePlayerControl((Mob)(Object)this);
        if (living != null) {
            cir.setReturnValue(living);
        }
    }
}
