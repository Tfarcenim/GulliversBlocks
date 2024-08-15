package tfar.gulliversblocks.effect;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import tfar.gulliversblocks.LivingEntityDuck;

public class UpsizingEffect extends MobEffect {

    protected UpsizingEffect(MobEffectCategory pCategory, int pColor, ParticleOptions pParticle) {
        super(pCategory, pColor, pParticle);
    }

    public UpsizingEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
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
        int add = pAmplifier+1;
        LivingEntityDuck.of(pLivingEntity).addGulliverScale(add);
    }

}
