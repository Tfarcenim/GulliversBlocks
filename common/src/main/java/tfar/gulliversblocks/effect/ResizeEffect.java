package tfar.gulliversblocks.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import tfar.gulliversblocks.LivingEntityDuck;

public class ResizeEffect extends MobEffect {

    protected final boolean growing;

//    protected ResizeEffect(MobEffectCategory pCategory, int pColor, ParticleOptions pParticle,boolean) {
  //      super(pCategory, pColor, pParticle);
  //  }

    public ResizeEffect(MobEffectCategory pCategory, int pColor,boolean growing) {
        super(pCategory, pColor);
        this.growing = growing;
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int pDuration, int pAmplifier) {
        return pDuration >= 1;
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {
        int add = (pAmplifier+1) * (growing ? 1 : -1);
        LivingEntityDuck.of(pLivingEntity).addGulliverScale(add);
    }

}
