package tfar.gulliversblocks.datagen.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.advancements.ForceRideTrigger;
import tfar.gulliversblocks.advancements.PickedUpTrigger;
import tfar.gulliversblocks.advancements.ReachSizeTrigger;
import tfar.gulliversblocks.TextComponents;
import tfar.gulliversblocks.init.ModPotions;

import java.util.Optional;
import java.util.function.Consumer;
//I want there to be a few advancements.
//"Mixologist Extraordinaire"
//Brew all 3 new potions
//"Bigger and Better"
//Drinking an Embiggening potion.
//"Down to Size"
//Drinking an Ensmallening potion.
//"Ant"
//Shrinking to 1/4th a block tall.
//"Titan"
//Growing to 16 blocks tall.
//"Unlikely Steed"
//Riding any player/mob at a different size using string.
//"A Giant’s Grasp"
//Being picked up by any player/mob at a different size
//"It’s-a Me!"
//Eat a Super Mushroom (If added)
//"Tiny Toadstool"
//Eat a Mini Mushroom (If added)
public class ModAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder advancementholder = Advancement.Builder.advancement()
                .display(
                        Blocks.RED_NETHER_BRICKS,
                        TextComponents.ADVANCEMENT_ROOT,
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
                .addCriterion("upsize",CriteriaTriggers.BREWED_POTION.createCriterion(new BrewedPotionTrigger.TriggerInstance(Optional.empty(), Optional.of(ModPotions.UPSIZE_II))))
                .addCriterion("downsize",CriteriaTriggers.BREWED_POTION.createCriterion(new BrewedPotionTrigger.TriggerInstance(Optional.empty(), Optional.of(ModPotions.DOWNSIZE_II))))
                .addCriterion("renewal",CriteriaTriggers.BREWED_POTION.createCriterion(new BrewedPotionTrigger.TriggerInstance(Optional.empty(), Optional.of(ModPotions.RENEWAL))))
                .save(saver, GulliversBlocks.id("brew_size_potion"),existingFileHelper);

        AdvancementHolder big = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.BIGGER_AND_BETTER,
                        TextComponents.BIGGER_AND_BETTER_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
             //   .addCriterion("effect", EffectsChangedTrigger.TriggerInstance.hasEffects(
           //                     MobEffectsPredicate.Builder.effects()
            //                            .and(ModMobEffects.UPSIZE)))
                .addCriterion("potion_1", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.UPSIZE_I)).build())))

                .addCriterion("potion_2", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.UPSIZE_II)).build())))

                .addCriterion("potion_3", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.UPSIZE_III)).build())))

                .addCriterion("potion_4", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.UPSIZE_IV)).build())))

                .addCriterion("potion_5", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.UPSIZE_V)).build())))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver,GulliversBlocks.id("bigger_and_better"),existingFileHelper);


        AdvancementHolder small = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.DOWN_TO_SIZE,
                        TextComponents.DOWN_TO_SIZE_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("potion_1", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.DOWNSIZE_I)).build())))

                .addCriterion("potion_2", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.DOWNSIZE_II)).build())))

                .addCriterion("potion_3", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.DOWNSIZE_III)).build())))

                .addCriterion("potion_4", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.DOWNSIZE_IV)).build())))

                .addCriterion("potion_5", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item()
                        .hasComponents(DataComponentPredicate.builder().expect(DataComponents.POTION_CONTENTS,new PotionContents(ModPotions.DOWNSIZE_V)).build())))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver,GulliversBlocks.id("down_to_size"),existingFileHelper);

        AdvancementHolder ant = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.ANT,
                        TextComponents.ANT_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("titan", ReachSizeTrigger.TriggerInstance.reachSize(MinMaxBounds.Doubles.atMost(.25)))
                .save(saver, GulliversBlocks.id("ant"),existingFileHelper);

        AdvancementHolder titan = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.TITAN,
                        TextComponents.TITAN_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("titan", ReachSizeTrigger.TriggerInstance.reachSize(MinMaxBounds.Doubles.atLeast(4)))
                .save(saver, GulliversBlocks.id("titan"),existingFileHelper);


        AdvancementHolder a_giants_grasp = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.A_GIANTS_GRASP,
                        TextComponents.A_GIANTS_GRASP_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("giant", PickedUpTrigger.TriggerInstance.playerPickedUp(EntityPredicate.Builder.entity()))
                .save(saver, GulliversBlocks.id("a_giants_grasp"),existingFileHelper);


        AdvancementHolder unlikely_steed = Advancement.Builder.advancement()
                .parent(advancementholder)
                .display(
                        Items.POTION,
                        TextComponents.UNLIKELY_STEED,
                        TextComponents.UNLIKELY_STEED_DESC,
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("steed", ForceRideTrigger.TriggerInstance.playerRiding(EntityPredicate.Builder.entity()))
                .save(saver, GulliversBlocks.id("unlikely_steed"),existingFileHelper);
    }
}
