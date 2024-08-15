package tfar.gulliversblocks.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.effect.RenewalEffect;
import tfar.gulliversblocks.effect.UpsizingEffect;

public class ModMobEffects {

    public static final Holder<MobEffect> UPSIZING = register("upsizing", new UpsizingEffect(MobEffectCategory.NEUTRAL,0xff_ff_ff));

    public static final Holder<MobEffect> RENEWAL = register("renewal", new RenewalEffect(MobEffectCategory.NEUTRAL,0xff_ff_ff));

    public static Holder.Reference<MobEffect> register(String path,MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,GulliversBlocks.id(path),effect);
    }

    public static void boot(){}


}
