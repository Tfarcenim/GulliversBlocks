package tfar.gulliversblocks.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.GulliverScales;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.LivingEntityDuck;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixinFabric {
    @Shadow public abstract void displayClientMessage(Component pChatComponent, boolean pActionBar);

    @Inject(method = "startSleepInBed",at = @At("HEAD"),cancellable = true)
    private void onSleep(BlockPos bedPos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
        ServerPlayer player = (ServerPlayer)(Object) this;
        double scale = GulliverScales.SCALES.get(LivingEntityDuck.of(player).gulliversBlocks$getGulliverScale());
        if (scale > GulliversBlocks.MAX_SLEEPING_SIZE) {
            displayClientMessage(Component.translatable("sleep.too_big"), true);
            cir.setReturnValue(Either.left(Player.BedSleepingProblem.OTHER_PROBLEM));
        }
    }
}
