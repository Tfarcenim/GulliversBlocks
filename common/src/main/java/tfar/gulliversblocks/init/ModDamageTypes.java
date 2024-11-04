package tfar.gulliversblocks.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import tfar.gulliversblocks.GulliversBlocks;

public class ModDamageTypes {
    public static ResourceKey<DamageType> ROSE = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GulliversBlocks.MOD_ID,"rose"));
}
