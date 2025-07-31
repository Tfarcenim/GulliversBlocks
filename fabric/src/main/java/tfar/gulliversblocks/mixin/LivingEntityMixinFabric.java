package tfar.gulliversblocks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.GulliversBlocksFabric;

import java.util.Optional;

@Mixin(LivingEntity.class)
@Debug(export = true)
public abstract class LivingEntityMixinFabric extends Entity {

    @Shadow @Deprecated public abstract boolean canBreatheUnderwater();

    @Shadow public float oAttackAnim;
    @Shadow public float attackAnim;

    @Shadow public abstract Optional<BlockPos> getSleepingPos();

    @Shadow protected abstract void setPosToBed(BlockPos p_21081_);

    @Shadow protected abstract int decreaseAirSupply(int pCurrentAir);

    @Shadow protected abstract int increaseAirSupply(int pCurrentAir);

    @Shadow private BlockPos lastPos;

    @Shadow protected abstract void onChangedBlock(ServerLevel pLevel, BlockPos pPos);

    @Shadow public float yHeadRot;

    @Shadow public float yBodyRot;

    @Shadow public int hurtTime;

    @Shadow public abstract boolean isDeadOrDying();

    @Shadow protected abstract void tickDeath();

    @Shadow protected int lastHurtByPlayerTime;

    @Shadow @Nullable
    protected Player lastHurtByPlayer;

    @Shadow @Nullable private LivingEntity lastHurtMob;

    @Shadow @Nullable private LivingEntity lastHurtByMob;

    @Shadow public abstract void setLastHurtByMob(LivingEntity pLivingEntity);

    @Shadow protected abstract void tickEffects();

    @Shadow protected float animStepO;

    @Shadow protected float animStep;

    @Shadow public float yBodyRotO;

    @Shadow public float yHeadRotO;

    @Shadow private int lastHurtByMobTimestamp;

    public LivingEntityMixinFabric(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "isInvulnerableTo",at = @At("RETURN"),cancellable = true)
    private void conditionalImmunity(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        boolean vanillaImmunity = cir.getReturnValue();
        boolean moddedImmunity = GulliversBlocks.conditionalImmunity(this,source,vanillaImmunity);
        if (vanillaImmunity != moddedImmunity) {
            cir.setReturnValue(moddedImmunity);
        }
    }

    /**
     * @return
     * @author
     * @reason
     * @Overwrite public void baseTick() {
     * LivingEntity living = (LivingEntity)(Object)this;
     * this.oAttackAnim = this.attackAnim;
     * if (this.firstTick) {
     * this.getSleepingPos().ifPresent(this::setPosToBed);
     * }
     * <p>
     * if (this.level() instanceof ServerLevel serverlevel) {
     * EnchantmentHelper.tickEffects(serverlevel, living);
     * }
     * <p>
     * super.baseTick();
     * this.level().getProfiler().push("livingEntityBaseTick");
     * if (this.fireImmune() || this.level().isClientSide) {
     * this.clearFire();
     * }
     * <p>
     * if (this.isAlive()) {
     * boolean flag = living instanceof Player;
     * if (!this.level().isClientSide) {
     * if (this.isInWall()) {
     * this.hurt(this.damageSources().inWall(), 1.0F);
     * } else if (flag && !this.level().getWorldBorder().isWithinBounds(this.getBoundingBox())) {
     * double d4 = this.level().getWorldBorder().getDistanceToBorder(this) + this.level().getWorldBorder().getDamageSafeZone();
     * if (d4 < 0.0) {
     * double d0 = this.level().getWorldBorder().getDamagePerBlock();
     * if (d0 > 0.0) {
     * this.hurt(this.damageSources().outOfBorder(), (float)Math.max(1, Mth.floor(-d4 * d0)));
     * }
     * }
     * }
     * }
     * <p>
     * int airSupply = this.getAirSupply();
     * GulliversBlocksFabric.onLivingBreathe(living, airSupply - this.decreaseAirSupply(airSupply), this.increaseAirSupply(airSupply) - airSupply);
     * <p>
     * if (this.level() instanceof ServerLevel serverlevel1) {
     * BlockPos blockpos = this.blockPosition();
     * if (!Objects.equal(this.lastPos, blockpos)) {
     * this.lastPos = blockpos;
     * this.onChangedBlock(serverlevel1, blockpos);
     * }
     * }
     * }
     * <p>
     * if (this.isAlive() && (this.isInWaterRainOrBubble() || this.isInPowderSnow)) {
     * this.extinguishFire();
     * }
     * <p>
     * if (this.hurtTime > 0) {
     * this.hurtTime--;
     * }
     * <p>
     * if (this.invulnerableTime > 0 && !(living instanceof ServerPlayer)) {
     * this.invulnerableTime--;
     * }
     * <p>
     * if (this.isDeadOrDying() && this.level().shouldTickDeath(this)) {
     * this.tickDeath();
     * }
     * <p>
     * if (this.lastHurtByPlayerTime > 0) {
     * this.lastHurtByPlayerTime--;
     * } else {
     * this.lastHurtByPlayer = null;
     * }
     * <p>
     * if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
     * this.lastHurtMob = null;
     * }
     * <p>
     * if (this.lastHurtByMob != null) {
     * if (!this.lastHurtByMob.isAlive()) {
     * this.setLastHurtByMob(null);
     * } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
     * this.setLastHurtByMob(null);
     * }
     * }
     * <p>
     * this.tickEffects();
     * this.animStepO = this.animStep;
     * this.yBodyRotO = this.yBodyRot;
     * this.yHeadRotO = this.yHeadRot;
     * this.yRotO = this.getYRot();
     * this.xRotO = this.getXRot();
     * this.level().getProfiler().pop();
     * }
     */

    //     * int airSupply = this.getAirSupply();
    //     * GulliversBlocksFabric.onLivingBreathe(living, airSupply - this.decreaseAirSupply(airSupply), this.increaseAirSupply(airSupply) - airSupply);
    @ModifyExpressionValue(method = "baseTick",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean replaceBreatheMethod(boolean original) {
        int airSupply = getAirSupply();
        GulliversBlocksFabric.onLivingBreathe((LivingEntity) (Object)this,airSupply-decreaseAirSupply(airSupply), this.increaseAirSupply(airSupply) - airSupply);
        return false;
    }

    @ModifyVariable(method = "getVisibilityPercent",at = @At("RETURN"))
    private double modifyVisibility(double original,@Nullable Entity lookingEntity) {
        return original * GulliversBlocks.getVisibilityMultiplier((LivingEntity)(Object)this,lookingEntity);
    }

    /*@ModifyConstant(method = "handleRelativeFrictionAndCalculateMovement",constant = @Constant(doubleValue = .2))
    double adjustSpeed(double constant) {
        return GulliversBlocks.changeClimbingSpeed((LivingEntity) (Object)this,constant);
    }*/


    @Inject(method = "onClimbable",at = @At("RETURN"),cancellable = true)
    private void isOnClimbable(CallbackInfoReturnable<Boolean> cir) {
        boolean vanillaClimbable = cir.getReturnValue();
        if (vanillaClimbable) {
            return;
        }

        if (GulliversBlocks.canClimb((LivingEntity) (Object)this,getInBlockState())) {
            cir.setReturnValue(true);
        }
    }
}