package tfar.gulliversblocks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import tfar.gulliversblocks.init.ModPotions;

public class GulliversBlocksFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        GulliversBlocks.register();
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.STRENGTH, Items.BONE_MEAL,ModPotions.UPSIZE_II);
            builder.addMix(Potions.WEAKNESS, Items.WIND_CHARGE,ModPotions.DOWNSIZE_II);

            builder.addMix(ModPotions.UPSIZE_II,Items.FERMENTED_SPIDER_EYE,ModPotions.UPSIZE_I);
            builder.addMix(ModPotions.UPSIZE_III,Items.FERMENTED_SPIDER_EYE,ModPotions.UPSIZE_II);
            builder.addMix(ModPotions.UPSIZE_IV,Items.FERMENTED_SPIDER_EYE,ModPotions.UPSIZE_III);
            builder.addMix(ModPotions.UPSIZE_V,Items.FERMENTED_SPIDER_EYE,ModPotions.UPSIZE_IV);

            builder.addMix(ModPotions.UPSIZE_I,Items.REDSTONE,ModPotions.UPSIZE_II);
            builder.addMix(ModPotions.UPSIZE_II,Items.REDSTONE,ModPotions.UPSIZE_III);
            builder.addMix(ModPotions.UPSIZE_III,Items.REDSTONE,ModPotions.UPSIZE_IV);
            builder.addMix(ModPotions.UPSIZE_IV,Items.REDSTONE,ModPotions.UPSIZE_V);

            builder.addMix(ModPotions.DOWNSIZE_II,Items.FERMENTED_SPIDER_EYE,ModPotions.DOWNSIZE_I);
            builder.addMix(ModPotions.DOWNSIZE_III,Items.FERMENTED_SPIDER_EYE,ModPotions.DOWNSIZE_II);
            builder.addMix(ModPotions.DOWNSIZE_IV,Items.FERMENTED_SPIDER_EYE,ModPotions.DOWNSIZE_III);
            builder.addMix(ModPotions.DOWNSIZE_V,Items.FERMENTED_SPIDER_EYE,ModPotions.DOWNSIZE_IV);

            builder.addMix(ModPotions.DOWNSIZE_I,Items.REDSTONE,ModPotions.DOWNSIZE_II);
            builder.addMix(ModPotions.DOWNSIZE_II,Items.REDSTONE,ModPotions.DOWNSIZE_III);
            builder.addMix(ModPotions.DOWNSIZE_III,Items.REDSTONE,ModPotions.DOWNSIZE_IV);
            builder.addMix(ModPotions.DOWNSIZE_IV,Items.REDSTONE,ModPotions.DOWNSIZE_V);

            builder.addMix(Potions.MUNDANE,Items.GLOWSTONE_DUST,ModPotions.RENEWAL);
        });
        // Use Fabric to bootstrap the Common mod.
        GulliversBlocks.init();
    }
}
