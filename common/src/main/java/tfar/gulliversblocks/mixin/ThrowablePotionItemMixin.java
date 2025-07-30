package tfar.gulliversblocks.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.gulliversblocks.GulliverMixinHooks;

@Mixin(ThrowablePotionItem.class)
public class ThrowablePotionItemMixin {

    @Inject(method = "use",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/ThrownPotion;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void onUseItem(Level pLevel, Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                           ItemStack itemstack, ThrownPotion thrownpotion)  {
        GulliverMixinHooks.onUseItem(pLevel, pPlayer, pHand, cir, itemstack, thrownpotion);
    }
}
