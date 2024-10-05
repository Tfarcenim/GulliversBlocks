package tfar.gulliversblocks;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import tfar.gulliversblocks.init.ModPotions;
import tfar.gulliversblocks.network.PacketHandler;

import java.util.Map;

public class GulliversBlocksFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        GulliversBlocks.register();
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> ModCommands.dispatcher(dispatcher));
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
        PacketHandler.registerPackets();
        UseEntityCallback.EVENT.register(this::interact);
    }

    InteractionResult interact(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (GulliversBlocks.canPickup(player,hand,entity)) {
            entity.startRiding(player);
            PlayerDuck playerDuck = PlayerDuck.of(player);
            HumanoidArm mainArm = player.getMainArm();
            Map<MountPosition, Entity> mountPos = playerDuck.getMountPositions();
            switch (mainArm) {
                case RIGHT -> {
                    switch (hand) {
                        case MAIN_HAND -> {
                            mountPos.put(MountPosition.RIGHT_HAND,entity);
                        }
                        case OFF_HAND -> {
                            mountPos.put(MountPosition.LEFT_HAND,entity);
                        }
                    }
                }
                case LEFT -> {
                    switch (hand) {
                        case MAIN_HAND -> {
                            mountPos.put(MountPosition.LEFT_HAND,entity);

                        }
                        case OFF_HAND -> {
                            mountPos.put(MountPosition.RIGHT_HAND,entity);
                        }
                    }
                }
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return InteractionResult.PASS;
    }

}
