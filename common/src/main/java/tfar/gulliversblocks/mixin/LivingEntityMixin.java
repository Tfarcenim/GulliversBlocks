package tfar.gulliversblocks.mixin;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.duck.LivingEntityDuck;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityDuck {
    @Shadow public abstract void push(Entity pEntity);

    @Shadow public abstract double getAttributeValue(Holder<Attribute> pAttribute);

    @Unique
    Map<MountPosition,Entity> gulliversBlocks$mountPositions = new EnumMap<>(MountPosition.class);

    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        super.positionRider(passenger, callback);
    }

    @Override
    public Map<MountPosition, Entity> getMountPositions() {
        return Collections.unmodifiableMap(gulliversBlocks$mountPositions);
    }

    @Override
    public void addMount(MountPosition position, Entity entity) {
        gulliversBlocks$mountPositions.put(position, entity);
        if (GulliversBlocks.DEV) {
            GulliversBlocks.LOG.info("{} picked up {} in position {}",this,entity,position);
        }
    }

    @Override
    public void removeMount(MountPosition position) {
        Entity remove = gulliversBlocks$mountPositions.remove(position);
        if (GulliversBlocks.DEV) {
            GulliversBlocks.LOG.info("{} removed {} from position {}",this,remove,position);
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pPartialTick) {
        MountPosition mountPos = null;
        for (Map.Entry<MountPosition,Entity> entry: gulliversBlocks$mountPositions.entrySet()) {
            if (entry.getValue() == pEntity) {
                mountPos = entry.getKey();
            }
        }

        if (mountPos == null) {
            return super.getPassengerAttachmentPoint(pEntity, pDimensions, pPartialTick);
        }

        return GulliversBlocks.repositionRiders((LivingEntity)(Object)this,pEntity,pDimensions,pPartialTick,mountPos);
    }

    @Inject(method = "serverAiStep",at = @At("HEAD"))
    private void onEntityTick(CallbackInfo ci) {
        GulliversBlocks.onLivingTick((LivingEntity) (Object)this);
    }

    @Unique
    int gulliversBlocks$gulliverScale;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public int gulliversBlocks$getGulliverScale() {
        return gulliversBlocks$gulliverScale;
    }

    @Override
    public void gulliversBlocks$setGulliverScale(int gulliverScale) {
        if (gulliverScale != gulliversBlocks$gulliverScale) {
            if (!this.level().isClientSide) {
                GulliversBlocks.onGulliverScaleChange((LivingEntity) (Object) this, gulliversBlocks$gulliverScale, gulliverScale);
            }
            this.gulliversBlocks$gulliverScale = gulliverScale;
        }
    }

    @Inject(method = "readAdditionalSaveData",at = @At("HEAD"))
    private void read(CompoundTag pCompound, CallbackInfo ci) {
        gulliversBlocks$gulliverScale = pCompound.getInt("gulliver_scale");
    }

    @Inject(method = "addAdditionalSaveData",at = @At("HEAD"))
    private void save(CompoundTag pCompound, CallbackInfo ci){
        pCompound.putInt("gulliver_scale",gulliversBlocks$gulliverScale);
    }

    @Inject(method = "doPush",at = @At("RETURN"))
    private void onPushed(Entity pushed, CallbackInfo ci) {
        GulliversBlocks.onPushed((LivingEntity) (Object)this,pushed);
    }

    @Inject(method = "getRiddenInput",at = @At("HEAD"),cancellable = true)
    private void getRidingVector(Player pPlayer, Vec3 pTravelVector, CallbackInfoReturnable<Vec3> cir) {
        if ((Object)this instanceof Mob mob) {
            Vec3 control = GulliversBlocks.getRideVector(mob,pPlayer);
            if (control != null) {
                cir.setReturnValue(control);
            }
        }
    }

    @Inject(method = "getRiddenSpeed",at = @At("HEAD"),cancellable = true)
    private void setRideSpeed(Player pPlayer, CallbackInfoReturnable<Float> cir) {
        if ((Object)this instanceof Parrot) {
            cir.setReturnValue((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        }
    }

    @Inject(method = "tickRidden",at = @At("RETURN"))
    private void onTickRidden(Player pPlayer, Vec3 pTravelVector, CallbackInfo ci) {
        GulliversBlocks.onTickRidden((LivingEntity)(Object)this,pPlayer,pTravelVector);
    }
}