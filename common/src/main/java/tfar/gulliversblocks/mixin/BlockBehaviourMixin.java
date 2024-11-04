package tfar.gulliversblocks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.GulliversBlocks;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

    @Inject(method = "entityInside",at = @At("HEAD"))
    private void onInsideBlock(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity, CallbackInfo ci) {
        GulliversBlocks.onInsideBlock((BlockBehaviour)(Object)this,pState, pLevel, pPos, pEntity);
    }

    @Inject(method = "getCollisionShape",at = @At("RETURN"),cancellable = true)
    private void smallEffect(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        GulliversBlocks.onCollide((BlockBehaviour) (Object)this,pState,pLevel,pPos,pContext,cir);
    }
}
