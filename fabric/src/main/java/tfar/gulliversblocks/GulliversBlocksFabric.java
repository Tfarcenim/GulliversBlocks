package tfar.gulliversblocks;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.config.ModConfig;
import tfar.gulliversblocks.config.GulliversBlocksConfig;
import tfar.gulliversblocks.events.LivingWaterCallbacks;
import tfar.gulliversblocks.init.ModPotions;
import tfar.gulliversblocks.network.PacketHandler;

public class GulliversBlocksFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        GulliversBlocks.register();
        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) -> ModCommands.dispatcher(dispatcher));
        ServerPlayerEvents.COPY_FROM.register(GulliversBlocks::copyFrom);
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
        UseEntityCallback.EVENT.register((player, world, hand, entity1, hitResult) -> GulliversBlocks.entityInteract(player, world, hand, entity1));

        LivingWaterCallbacks.BREATHING.register((entity, result) -> {
            if(entity.level().isRainingAt(entity.blockPosition()) && entity.getBbHeight() <= GulliversBlocksConfig.Server.DROWN_IN_RAIN_SIZE.get() &&
                    entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && !(MobEffectUtil.hasWaterBreathing(entity) || entity.canBreatheUnderwater())) {
                result.setCanBreathe(false);
            }
        });

        GulliversBlocks.setup();

        NeoForgeConfigRegistry.INSTANCE.register(GulliversBlocks.MOD_ID, ModConfig.Type.SERVER,GulliversBlocksConfig.SERVER_SPEC);
    }

    //fabric implementation of neoforge event, see CommonHooks
    public static void onLivingBreathe(LivingEntity entity, int consumeAirAmount, int refillAirAmount) {
        // Check things that vanilla considers to be air - these will cause the air supply to be increased.
        boolean isAir = !entity.isEyeInFluid(FluidTags.WATER)  || entity.level().getBlockState(BlockPos.containing(entity.getX(), entity.getEyeY(), entity.getZ())).is(Blocks.BUBBLE_COLUMN);
        boolean canBreathe = isAir;
        // The following effects cause the entity to not drown, but do not cause the air supply to be increased.
        if (!isAir && (MobEffectUtil.hasWaterBreathing(entity) || entity.canBreatheUnderwater() || entity instanceof Player player && player.getAbilities().invulnerable)) {
            canBreathe = true;
            refillAirAmount = 0;
        }
       // LivingBreatheEvent breatheEvent = new LivingBreatheEvent(entity, canBreathe, consumeAirAmount, refillAirAmount);

        LivingWaterCallbacks.BreathResult breathResult = new LivingWaterCallbacks.BreathResult(canBreathe,consumeAirAmount,refillAirAmount);

        LivingWaterCallbacks.BREATHING.invoker().onBreath(entity, breathResult);

        //NeoForge.EVENT_BUS.post(breatheEvent);
        if (breathResult.isCanBreathe()) {
            entity.setAirSupply(Math.min(entity.getAirSupply() + breathResult.getRefillAirAmount(), entity.getMaxAirSupply()));
        } else {
            entity.setAirSupply(entity.getAirSupply() - breathResult.getConsumeAirAmount());
        }

        if (entity.getAirSupply() <= 0) {
            LivingWaterCallbacks.DrownResult drownResult = new LivingWaterCallbacks.DrownResult( entity.getAirSupply() <= -20, 2.0F, 8);
            LivingWaterCallbacks.DROWNING.invoker().onDrown(entity,drownResult);
            //LivingDrownEvent drownEvent = new LivingDrownEvent(entity);
            if (drownResult.isDrowning()) {
                entity.setAirSupply(0);
                Vec3 vec3 = entity.getDeltaMovement();

                for (int i = 0; i < drownResult.getBubbleCount(); ++i) {
                    double d2 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    double d3 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    double d4 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                    entity.level().addParticle(ParticleTypes.BUBBLE, entity.getX() + d2, entity.getY() + d3, entity.getZ() + d4, vec3.x, vec3.y, vec3.z);
                }

                if (drownResult.getDamageAmount() > 0) entity.hurt(entity.damageSources().drown(), drownResult.getDamageAmount());
            }
        }

        if (!isAir && !entity.level().isClientSide && entity.isPassenger() && entity.getVehicle() != null && entity.getVehicle().dismountsUnderwater()) {
            entity.stopRiding();
        }
    }

}
