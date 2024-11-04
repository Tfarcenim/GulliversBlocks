package tfar.gulliversblocks.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.gulliversblocks.GulliversBlocks;

@Mixin(Player.class)
public class PlayerMixinFabric {
    @Inject(method = "tick",at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        GulliversBlocks.tickPlayer((Player) (Object)this);
    }
}
