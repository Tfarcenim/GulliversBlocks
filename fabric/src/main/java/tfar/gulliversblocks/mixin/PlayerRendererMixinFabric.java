package tfar.gulliversblocks.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.gulliversblocks.client.ModClient;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixinFabric {

    @Inject(method = "renderRightHand",at = @At("HEAD"),cancellable = true)
    private void cancelRightArmRendering(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, CallbackInfo ci) {
        if (player.getMainArm() == HumanoidArm.RIGHT) {
            if (ModClient.onArmRender(player,HumanoidArm.RIGHT)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderLeftHand",at = @At("HEAD"),cancellable = true)
    private void cancelLeftArmRendering(PoseStack poseStack, MultiBufferSource buffer, int combinedLight, AbstractClientPlayer player, CallbackInfo ci) {
        if (player.getMainArm() == HumanoidArm.LEFT) {
            if (ModClient.onArmRender(player,HumanoidArm.LEFT)) {
                ci.cancel();
            }
        }
    }
}
