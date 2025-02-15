package tfar.gulliversblocks.mixin;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
}
