package tfar.gulliversblocks;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.init.CustomScaleModifiers;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleTypes;

public class GulliverMixinHooks {

    public static void onUseItem(Level pLevel, Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir,
                           ItemStack itemstack, ThrownPotion thrownpotion)  {
        ScaleData scaleData = ScaleTypes.MOTION.getScaleData(thrownpotion);
        float before = scaleData.getScale();
        scaleData.getBaseValueModifiers().add(CustomScaleModifiers.CUSTOM_MOTION);
        float after = scaleData.getScale();
        GulliversBlocks.LOG.info("Power before: {} Power after {}",before,after);
    }
}
