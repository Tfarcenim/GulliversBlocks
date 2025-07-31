package tfar.gulliversblocks.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.config.GulliversBlocksConfig;


@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "isIgnoringBlockTriggers",at = @At("RETURN"),cancellable = true)
    private void ignoreIfSmall(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof LivingEntity living) {
            if (living.getBbHeight() <= GulliversBlocksConfig.Server.PRESSURE_PLATE_SIZE.get()) {
                cir.setReturnValue(true);
            }
        }
    }
}
