package tfar.gulliversblocks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.gulliversblocks.client.ModClient;
import tfar.gulliversblocks.client.ModClientFabric;

@Mixin(Minecraft.class)
public class MinecraftMixinFabric {


    @Shadow @Nullable public HitResult hitResult;

    @Shadow @Nullable
    public LocalPlayer player;

    @Inject(method = "startUseItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void rightClickAir(CallbackInfo ci, InteractionHand[] var1, int var2, int var3, InteractionHand interactionHand, ItemStack itemStack) {
        if (itemStack.isEmpty() && (this.hitResult == null || this.hitResult.getType() == HitResult.Type.MISS)) {
            ModClient.onRightClickEmpty(player, interactionHand);
        }
    }

    @Inject(method = "handleKeybinds",at = @At("HEAD"))
    private void onOffHandPressed(CallbackInfo ci) {
        ModClient.interceptKeybinds((Minecraft)(Object)this);
    }
}
