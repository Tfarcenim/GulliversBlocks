package tfar.gulliversblocks.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import tfar.gulliversblocks.GulliversBlocks;

public class ModPotions {
    public static final Holder<Potion> UPSIZE_I = register("upsize_i", new Potion(new MobEffectInstance(ModMobEffects.UPSIZE,1)));
    public static final Holder<Potion> UPSIZE_II = register("upsize_ii", new Potion(new MobEffectInstance(ModMobEffects.UPSIZE,1,1)));
    public static final Holder<Potion> UPSIZE_III = register("upsize_iii", new Potion(new MobEffectInstance(ModMobEffects.UPSIZE,1,2)));
    public static final Holder<Potion> UPSIZE_IV = register("upsize_iv", new Potion(new MobEffectInstance(ModMobEffects.UPSIZE,1,3)));
    public static final Holder<Potion> UPSIZE_V = register("upsize_v", new Potion(new MobEffectInstance(ModMobEffects.UPSIZE,1,4)));

    public static final Holder<Potion> DOWNSIZE_I = register("downsize_i", new Potion(new MobEffectInstance(ModMobEffects.DOWNSIZE,1)));
    public static final Holder<Potion> DOWNSIZE_II = register("downsize_ii", new Potion(new MobEffectInstance(ModMobEffects.DOWNSIZE,1,1)));
    public static final Holder<Potion> DOWNSIZE_III = register("downsize_iii", new Potion(new MobEffectInstance(ModMobEffects.DOWNSIZE,1,2)));
    public static final Holder<Potion> DOWNSIZE_IV = register("downsize_iv", new Potion(new MobEffectInstance(ModMobEffects.DOWNSIZE,1,3)));
    public static final Holder<Potion> DOWNSIZE_V = register("downsize_v", new Potion(new MobEffectInstance(ModMobEffects.DOWNSIZE,1,4)));

    public static final Holder<Potion> RENEWAL = register("renewal",new Potion(new MobEffectInstance(ModMobEffects.RENEWAL)));

    public static Holder.Reference<Potion> register(String path, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, GulliversBlocks.id(path),potion);
    }

    public static void boot(){}

}
