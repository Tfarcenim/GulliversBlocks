package tfar.gulliversblocks.init;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import tfar.gulliversblocks.GulliversBlocks;

public class ModPotions {
    public static final Holder<Potion> UPSIZING_I = register("upsizing_i", new Potion(new MobEffectInstance(ModMobEffects.UPSIZING,1)));
    public static final Holder<Potion> UPSIZING_II = register("upsizing_ii", new Potion(new MobEffectInstance(ModMobEffects.UPSIZING,1,1)));
    public static final Holder<Potion> UPSIZING_III = register("upsizing_iii", new Potion(new MobEffectInstance(ModMobEffects.UPSIZING,1,2)));
    public static final Holder<Potion> UPSIZING_IV = register("upsizing_iv", new Potion(new MobEffectInstance(ModMobEffects.UPSIZING,1,3)));
    public static final Holder<Potion> UPSIZING_V = register("upsizing_v", new Potion(new MobEffectInstance(ModMobEffects.UPSIZING,1,4)));

    public static final Holder<Potion> RENEWAL = register("renewal",new Potion(new MobEffectInstance(ModMobEffects.RENEWAL)));

    public static Holder.Reference<Potion> register(String path, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, GulliversBlocks.id(path),potion);
    }

    public static void boot(){}

}
