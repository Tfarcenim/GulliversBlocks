package tfar.gulliversblocks.datagen.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.TextComponents;

import java.util.function.Consumer;
//I want there to be a few advancements.
//“Mixologist Extraordinaire”
//Brew all 3 new potions
//“Bigger and Better”
//Drinking an Embiggening potion.
//“Down to Size”
//Drinking an Ensmallening potion.
//“Ant”
//Shrinking to 1/4th a block tall.
//“Titan”
//Growing to 16 blocks tall.
//“Unlikely Steed”
//Riding any player/mob at a different size using string.
//“A Giant’s Grasp”
//Being picked up by any player/mob at a different size
//“It’s-a Me!”
//Eat a Super Mushroom (If added)
//“Tiny Toadstool”
//Eat a Mini Mushroom (If added)
public class ModAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder advancementholder = Advancement.Builder.advancement()
                .display(
                        Blocks.RED_NETHER_BRICKS,
                        Component.translatable("advancements.nether.root.title"),
                        Component.translatable("advancements.nether.root.description"),
                        ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/nether.png"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("entered_nether", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER))
                .save(saver,GulliversBlocks.id("root"), existingFileHelper);

        AdvancementHolder advancementholder7 = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.MIXOLOGIST_EXTRAORDINAIRE,
                        TextComponents.MIXOLOGIST_EXTRAORDINAIRE_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("potion", BrewedPotionTrigger.TriggerInstance.brewedPotion())
                .save(saver, GulliversBlocks.id("nether/brew_potion"),existingFileHelper);
    }
}
