package tfar.gulliversblocks.mixin;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.GulliversBlocks;

//@Debug(export = true)
@Mixin(Entity.class)
public class EntityMixinFabric {
    @Shadow @Nullable
    private Entity vehicle;

    @Inject(method = "removeVehicle",at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;vehicle:Lnet/minecraft/world/entity/Entity;",ordinal = 2))
    private void onStopRiding(CallbackInfo ci) {
        GulliversBlocks.onStopRiding((Entity)(Object)this,vehicle);
    }

    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;Z)Z",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;canRide(Lnet/minecraft/world/entity/Entity;)Z"),cancellable = true)
    private void onAttemptRide(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        boolean b = GulliversBlocks.onAttemptRide((Entity) (Object) this, vehicle);
        if (!b) cir.setReturnValue(false);
    }
}
