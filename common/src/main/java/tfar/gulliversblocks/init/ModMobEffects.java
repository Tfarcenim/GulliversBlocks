package tfar.gulliversblocks.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.effect.RenewalEffect;
import tfar.gulliversblocks.effect.ResizeEffect;

public class ModMobEffects {

    public static final Holder<MobEffect> UPSIZE = register("upsize", new ResizeEffect(MobEffectCategory.NEUTRAL,0x8F71E2,true));
    public static final Holder<MobEffect> DOWNSIZE = register("downsize", new ResizeEffect(MobEffectCategory.NEUTRAL,0x8DE271,false));

    public static final Holder<MobEffect> RENEWAL = register("renewal", new RenewalEffect(MobEffectCategory.NEUTRAL,0xFFDD7F));

    public static Holder.Reference<MobEffect> register(String path,MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,GulliversBlocks.id(path),effect);
    }

    public static void boot(){}


}
