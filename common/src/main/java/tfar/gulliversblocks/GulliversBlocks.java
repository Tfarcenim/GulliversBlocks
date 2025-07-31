package tfar.gulliversblocks;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.gulliversblocks.config.GulliversBlocksConfig.Server;
import tfar.gulliversblocks.duck.LivingEntityDuck;
import tfar.gulliversblocks.init.*;
import tfar.gulliversblocks.network.client.S2CDropEntityPacket;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.network.client.S2CSetMountPositionPacket;
import tfar.gulliversblocks.platform.Services;
import virtuoel.pehkui.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class GulliversBlocks {

    public static final String MOD_ID = "gulliversblocks";
    public static final String MOD_NAME = "Gulliver's Blocks";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final boolean DEV = Services.PLATFORM.isDevelopmentEnvironment();

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        InteractionEvent.FARMLAND_TRAMPLE.register((world, pos, state, distance, entity) -> {
            if (entity instanceof LivingEntity living) {
                if (living.getBbHeight() <= Server.TRAMPLE_FARMLAND_SIZE.get()) {
                    return EventResult.interruptFalse();
                }
            }
            return EventResult.pass();
        });
        InteractionEvent.RIGHT_CLICK_ITEM.register((player, interactionHand) -> {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (stack.getItem() instanceof FishingRodItem) {
                if (player.fishing != null && Server.SCALES.get().get(LivingEntityDuck.of(player).gulliversBlocks$getGulliverScale()) <= Server.FISHING_ROD_GRAPPLE_SCALE.get()) {
                    Vec3 playerPos = player.position();
                    Vec3 fishingPos = player.fishing.position();
                    Vec3 dist = fishingPos.subtract(playerPos);
                    player.setDeltaMovement(player.getDeltaMovement().add(dist.normalize().scale(4)));
                    player.hurtMarked = true;
                }
            }
            return CompoundEventResult.pass();
        });


        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, interactionHand, blockPos, direction) -> {
            List<Entity> passengers = player.getPassengers();
            if (!player.level().isClientSide && !passengers.isEmpty()) {
                LivingEntityDuck duck = LivingEntityDuck.of(player);
                MountPosition position = getPosition(player,interactionHand);
                if (duck.getMountPositions().containsKey(position)) {
                    Entity entity = duck.getMountPositions().get(position);
                    entity.stopRiding();
                    entity.setPos(blockPos.relative(direction).getBottomCenter());
                    return EventResult.interruptDefault();
                }
            }
            return EventResult.pass();
        });

        //Cnan run clientside
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {

            List<Entity> passengers = livingEntity.getPassengers();

            if (!passengers.isEmpty()) {
                checkDrop(livingEntity, MountPosition.RIGHT_HAND);
                checkDrop(livingEntity, MountPosition.LEFT_HAND);
            }

            Entity attacker = damageSource.getDirectEntity();
            if (attacker instanceof LivingEntity livingAttacker) {
                if (passengers.contains(livingAttacker) && livingAttacker.getMainHandItem().is(ModTags.Items.PREVENTS_DISMOUNT)) {
                    return EventResult.interruptFalse();
                }
            }

            return EventResult.pass();
        });
    }

    public static void setup() {
        EntityType.POTION.clientTrackingRange = 8;
    }

    public static void checkDrop(LivingEntity living, MountPosition mountPosition) {
        if (living.level().isClientSide)return;
        LivingEntityDuck livingEntityDuck = LivingEntityDuck.of(living);

        Map<MountPosition, Entity> mountPositions = livingEntityDuck.getMountPositions();
        Entity rightHandEntity = mountPositions.get(mountPosition);
        if (rightHandEntity != null) {
            boolean shouldDrop = true;
            if (rightHandEntity instanceof LivingEntity livingRightHandEntity) {
                shouldDrop = !eitherHandHas(livingRightHandEntity, stack -> stack.is(ModTags.Items.PREVENTS_DISMOUNT));
            }

            if (shouldDrop) {
                rightHandEntity.stopRiding();
                livingEntityDuck.removeMount(mountPosition);
                S2CRemoveMountPositionPacket.sendToTracking(living, mountPosition);
            }
        }
    }

    public static boolean eitherHandHas(LivingEntity living, Predicate<ItemStack> stackPredicate) {
        return stackPredicate.test(living.getMainHandItem()) || stackPredicate.test(living.getOffhandItem());
    }

    public static MountPosition getPosition(LivingEntity entity,InteractionHand hand) {
        HumanoidArm mainArm = entity.getMainArm();
        return switch (mainArm){
            case RIGHT -> switch (hand) {
                case OFF_HAND -> MountPosition.LEFT_HAND;
                case MAIN_HAND -> MountPosition.RIGHT_HAND;
            };
            case LEFT -> switch (hand) {
                case OFF_HAND -> MountPosition.RIGHT_HAND;
                case MAIN_HAND -> MountPosition.LEFT_HAND;
            };
        };
    }

    public static void register() {
        ModMobEffects.boot();
        ModPotions.boot();
        ModCriteriaTriggers.boot();
        CustomScaleModifiers.init();
    }


    //public static final UUID GULLIVER = UUID.fromString("fbccf38e-8c5e-495a-a269-1ee614baef61");
    public static final ResourceLocation MODIFIER_ID = GulliversBlocks.id("attribute_modifier");

    public static void onGulliverScaleChange(LivingEntity living, int oldScale, int newScale) {
        if (newScale == 0) {
            for (ScaleType type : ScaleRegistries.SCALE_TYPES.values()) {
                ScaleData data = type.getScaleData(living);
                Boolean persist = data.getPersistence();
                data.resetScale();
                data.getBaseValueModifiers().removeIf(scaleModifier -> scaleModifier instanceof GulliverScaleModifier);
                data.setPersistence(persist);
            }

            AttributeMap attributes = living.getAttributes();
            for (Map.Entry<Holder<Attribute>, AttributeInstance> entry : attributes.attributes.entrySet()) {
                Holder<Attribute> attributeHolder = entry.getKey();
                AttributeInstance instance = entry.getValue();
                instance.removeModifier(MODIFIER_ID);
            }

        } else {

            if (GulliverScales.valid(newScale)) {
                double gulliverScale = Server.SCALES.get().getOrDefault(newScale, 1d);
                ScaleData scaleData = ScaleTypes.BASE.getScaleData(living);
                scaleData.setScaleTickDelay(40);
                scaleData.setPersistence(true);
                scaleData.setTargetScale((float) gulliverScale);
                //scaleData.getBaseValueModifiers().add(CustomScaleModifiers.CUSTOM_MOTION);

                if (living instanceof Player player) {
                    //multiplying by -1 is 0
                    double speedModifier = Server.BLOCK_BREAK_SPEED_SCALING.get().function.applyAsDouble(gulliverScale);
                    addAttributeMultSafely(player, Attributes.BLOCK_BREAK_SPEED, speedModifier);
                }

                double maxHealthModifier = Math.max(Server.MINIMUM_MAX_HEALTH_SCALE.get(), Server.MAX_HEALTH_SCALING.get().function.applyAsDouble(gulliverScale));
                addAttributeMultSafely(living, Attributes.MAX_HEALTH, maxHealthModifier);

                double attackDamageModifier = Server.ATTACK_DAMAGE_SCALING.get().function.applyAsDouble(gulliverScale);
                addAttributeMultSafely(living, Attributes.ATTACK_DAMAGE, attackDamageModifier);

                double movementModifier = Server.MOVEMENT_SPEED_SCALING.get().function.applyAsDouble(gulliverScale);
                addAttributeMultSafely(living, Attributes.MOVEMENT_SPEED, movementModifier);

                double fallDamageScaling = Server.FALL_DAMAGE_MULTIPLIER_SCALING.get().function.applyAsDouble(gulliverScale);
                addAttributeMultSafely(living, Attributes.FALL_DAMAGE_MULTIPLIER, fallDamageScaling);

                double safeFallScaling = Server.SAFE_FALL_DISTANCE_SCALING.get().function.applyAsDouble(gulliverScale);
                addAttributeMultSafely(living, Attributes.SAFE_FALL_DISTANCE, safeFallScaling);

                double gravityScaling = Server.GRAVITY_SCALING.get().function.applyAsDouble(gulliverScale);
                addAttributeMultSafely(living, Attributes.GRAVITY, gravityScaling);

                double jumpScaling = Server.JUMP_SCALING.get().function.applyAsDouble(gulliverScale);
                addAttributeMultSafely(living, Attributes.JUMP_STRENGTH, jumpScaling);
                if (living instanceof ServerPlayer player) {
                    ModCriteriaTriggers.REACH_SIZE.trigger(player, gulliverScale);
                }
            } else {
                GulliversBlocks.LOG.warn("Tried to set gulliver scale out of bounds {}", newScale);
            }
        }
        if (living.getHealth() > living.getHealth()) {
            living.setHealth(living.getMaxHealth());
        }
    }

    public static final ResourceLocation HELD_ONLY = GulliversBlocks.id("held_only");

    public static void tickPlayer(Player player) {
        if (!player.level().isClientSide) {
            ItemStack hand = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            if (player.getBbHeight() <= Server.PAPER_FLOAT_SIZE.get() && eitherHandHas(player,stack -> stack.is(Items.PAPER))) {
                player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 5, 0, false, false));
            }
            if (hand.is(Items.STICK)) {
                if (player.getBbHeight() <= .5) {
                    AttributeModifier modifier = new AttributeModifier(HELD_ONLY, 2, AttributeModifier.Operation.ADD_VALUE);
                    ItemAttributeModifiers attributeModifiers = hand.get(DataComponents.ATTRIBUTE_MODIFIERS);
                    if (attributeModifiers != null) {
                        attributeModifiers = attributeModifiers.withModifierAdded(Attributes.ENTITY_INTERACTION_RANGE, modifier, EquipmentSlotGroup.MAINHAND)
                                .withModifierAdded(Attributes.BLOCK_INTERACTION_RANGE, modifier, EquipmentSlotGroup.MAINHAND);
                    } else {
                        attributeModifiers = ItemAttributeModifiers.builder().add(Attributes.ENTITY_INTERACTION_RANGE, modifier, EquipmentSlotGroup.MAINHAND)
                                .add(Attributes.BLOCK_INTERACTION_RANGE, modifier, EquipmentSlotGroup.MAINHAND).build();
                    }
                    hand.set(DataComponents.ATTRIBUTE_MODIFIERS, attributeModifiers);
                } else {
                    ItemAttributeModifiers attributeModifiers = hand.get(DataComponents.ATTRIBUTE_MODIFIERS);


                    if (attributeModifiers != null) {
                        attributeModifiers = removeModifier(attributeModifiers, HELD_ONLY);
                    }
                    hand.set(DataComponents.ATTRIBUTE_MODIFIERS, attributeModifiers);
                }
            }
        }
    }

    public static ItemAttributeModifiers removeModifier(ItemAttributeModifiers oldModifiers, ResourceLocation modifierID) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        for (ItemAttributeModifiers.Entry entry : oldModifiers.modifiers()) {
            if (!entry.modifier().is(modifierID)) {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }
        return builder.build();
    }

    public static void copyFrom(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        LivingEntityDuck oldPlayerDuck = LivingEntityDuck.of(oldPlayer);
        LivingEntityDuck newPlayerDuck = LivingEntityDuck.of(newPlayer);

        newPlayerDuck.gulliversBlocks$setGulliverScale(oldPlayerDuck.gulliversBlocks$getGulliverScale());

    }

    public static void addAttributeSafely(LivingEntity entity, Holder<Attribute> attribute, AttributeModifier modifier) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance != null) {
            AttributeModifier old = attributeInstance.getModifier(modifier.id());
            if (old != null) {
                attributeInstance.removeModifier(modifier.id());
            }
            attributeInstance.addPermanentModifier(modifier);
        }
    }

    public static void addAttributeMultSafely(LivingEntity entity, Holder<Attribute> attribute, double value) {
        addAttributeSafely(entity, attribute, new AttributeModifier(MODIFIER_ID, value - 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    public static Vec3 repositionRiders(LivingEntity livingEntity, Entity pEntity, EntityDimensions pDimensions, float pPartialTick, MountPosition mountPosition) {

        switch (mountPosition) {
            case LEFT_SHOULDER -> {
                float z = 0.475f * pDimensions.width();
                int j = 1;
                float x = j * .575f * pDimensions.width();
                float y = .875f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-livingEntity.yBodyRot * (float) (Math.PI / 180.0));
            }
            case RIGHT_SHOULDER -> {
                float z = 0 * pDimensions.width();
                int j = -1;
                float x = j * .600f * pDimensions.width();
                float y = .8f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-livingEntity.yBodyRot * (float) (Math.PI / 180.0));
            }
            case LEFT_HAND -> {
                float z = 0.475f * pDimensions.width();
                int j = 1;//
                float x = j * .575f * pDimensions.width();
                float y = .375f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-livingEntity.yBodyRot * (float) (Math.PI / 180.0));
            }
            case RIGHT_HAND -> {
                float z = 0.475f * pDimensions.width();
                int j = -1;//
                float x = j * .575f * pDimensions.width();
                float y = .375f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-livingEntity.yBodyRot * (float) (Math.PI / 180.0));
            }
            case TOP -> {
                float z = 0;
                float x = 0;
                float y = pDimensions.height();
                return new Vec3(x, y, z)
                        .yRot(-livingEntity.yBodyRot * (float) (Math.PI / 180.0));
            }
        }
        throw new RuntimeException("Unexpected mountpos:" + mountPosition);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    static boolean canPickup(LivingEntity vehicle, InteractionHand hand, Entity rider) {
        LivingEntityDuck playerDuck = LivingEntityDuck.of(vehicle);
        HumanoidArm mainArm = vehicle.getMainArm();
        Map<MountPosition, Entity> mountPos = playerDuck.getMountPositions();
        switch (mainArm) {
            case RIGHT -> {
                switch (hand) {
                    case MAIN_HAND -> {
                        if (mountPos.get(MountPosition.RIGHT_HAND) != null) {
                            return false;
                        }
                    }
                    case OFF_HAND -> {
                        if (mountPos.get(MountPosition.LEFT_HAND) != null) {
                            return false;
                        }
                    }
                }
            }
            case LEFT -> {
                switch (hand) {
                    case MAIN_HAND -> {
                        if (mountPos.get(MountPosition.LEFT_HAND) != null) {
                            return false;
                        }
                    }
                    case OFF_HAND -> {
                        if (mountPos.get(MountPosition.RIGHT_HAND) != null) {
                            return false;
                        }
                    }
                }
            }
        }

        double ratio = getRatio(vehicle, rider);

        return ratio >= 6;
    }

    static boolean canRide(LivingEntity vehicle, Entity rider) {
        LivingEntityDuck playerDuck = LivingEntityDuck.of(vehicle);
        Map<MountPosition, Entity> mountPos = playerDuck.getMountPositions();
        if (mountPos.get(MountPosition.TOP) != null) {
            return false;
        }

        double ratio = getRatio(vehicle, rider);

        return ratio >= 6;
    }

    public static void swap(ServerPlayer player, MountPosition pos1, MountPosition pos2) {
        LivingEntityDuck playerDuck = LivingEntityDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
        Entity mount1 = mounts.get(pos1);
        Entity mount2 = mounts.get(pos2);

        if (mount2 != null) {
            playerDuck.addMount(pos1, mount2);
            S2CSetMountPositionPacket.sendToTracking(player, pos1, mount2);
        } else {
            playerDuck.removeMount(pos1);
            S2CRemoveMountPositionPacket.sendToTracking(player, pos1);
        }

        if (mount1 != null) {
            playerDuck.addMount(pos2, mount1);
            S2CSetMountPositionPacket.sendToTracking(player, pos2, mount1);
        } else {
            playerDuck.removeMount(pos2);
            S2CRemoveMountPositionPacket.sendToTracking(player, pos2);
        }
    }

    public static void throwEntity(Entity thrown, Player pShooter, float pX, float pY, float pZ, float pVelocity) {
        float f = -Mth.sin(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        float f1 = -Mth.sin((pX + pZ) * (float) (Math.PI / 180.0));
        float f2 = Mth.cos(pY * (float) (Math.PI / 180.0)) * Mth.cos(pX * (float) (Math.PI / 180.0));
        shoot(thrown, f, f1, f2, pVelocity);
        Vec3 vec3 = pShooter.getKnownMovement();
        thrown.setDeltaMovement(thrown.getDeltaMovement().add(vec3.x, pShooter.onGround() ? 0.0 : vec3.y, vec3.z));
    }


    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public static void shoot(Entity thrown, double pX, double pY, double pZ, float pVelocity) {
        Vec3 vec3 = new Vec3(pX, pY, pZ).normalize().scale(pVelocity);
        thrown.setDeltaMovement(vec3);
        thrown.hasImpulse = true;
        double d0 = vec3.horizontalDistance();
        thrown.setYRot((float) (Mth.atan2(vec3.x, vec3.z) * 180.0F / (float) Math.PI));
        thrown.setXRot((float) (Mth.atan2(vec3.y, d0) * 180.0F / (float) Math.PI));
        thrown.yRotO = thrown.getYRot();
        thrown.xRotO = thrown.getXRot();
    }

    public static double getVisibilityMultiplier(LivingEntity entity, @Nullable Entity lookingEntity) {
        double m = Server.SCALES.get().getOrDefault(LivingEntityDuck.of(entity).gulliversBlocks$getGulliverScale(), 1d);
        //   if (lookingEntity != null) {

        //      }
        return m;
    }

    public static boolean conditionalImmunity(Entity entity, DamageSource damageSource, boolean vanillaImmune) {
        if (damageSource.is(DamageTypes.CACTUS) && entity.getBbHeight() <= Server.CACTUS_PRICK_SIZE.get()) {
            return true;
        }
        return vanillaImmune;
    }

    public static void onInsideBlock(BlockBehaviour block, BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (block == Blocks.ROSE_BUSH) {
            if (pEntity.getBbHeight() <= Server.CACTUS_PRICK_SIZE.get()) {
                pEntity.hurt(pLevel.damageSources().source(ModDamageTypes.ROSE), 1.0F);
            }
        }
    }

    public static void onCollide(BlockBehaviour blockBehaviour, BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        if (pContext instanceof EntityCollisionContext entityCollisionContext) {
            Entity entity = entityCollisionContext.getEntity();
            if (entity != null) {
                if (blockBehaviour instanceof LeavesBlock) {
                    if (entity.getBbHeight() <= Server.CLIMB_BLOCKS_SIZE.get()) {
                        cir.setReturnValue(Shapes.empty());
                    }
                }
            }
        }
    }

    public static boolean canClimb(LivingEntity living, BlockState state) {
        if (living.getBbHeight() <= Server.CLIMB_BLOCKS_SIZE.get()) {
            if (state.is(ModTags.Blocks.CLIMBABLE_WHEN_SMALL)) {
                return true;
            }

            if (eitherHandHas(living,stack -> stack.is(Items.SLIME_BALL))) {
                Vec3 look = living.getLookAngle();
                Vec3 movementDirection = look.scale(.05);
                Vec3 pred = living.position().add(movementDirection.x, 0, movementDirection.z);
                BlockPos predPos = BlockPos.containing(pred);
                BlockState stateCollidedWith = living.level().getBlockState(predPos);
                if (!stateCollidedWith.getCollisionShape(living.level(), predPos).isEmpty()) {
                    return true;
                }
            }

            if (living.horizontalCollision) {
                Vec3 look = living.getLookAngle();
                Vec3 movementDirection = look.scale(.05);
                Vec3 pred = living.position().add(movementDirection.x, 0, movementDirection.z);
                BlockPos predPos = BlockPos.containing(pred);
                BlockState stateCollidedWith = living.level().getBlockState(predPos);
                if (stateCollidedWith.is(ModTags.Blocks.CLIMBABLE_WHEN_SMALL) && !stateCollidedWith.getCollisionShape(living.level(), predPos).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static double getRatio(Entity entity1, Entity entity2) {
        EntityDimensions entityDimensions1 = entity1.getDimensions(entity1.getPose());
        EntityDimensions entityDimensions = entity2.getDimensions(entity2.getPose());
        double entity1Volume = entityDimensions1.height() * entityDimensions1.width() * entityDimensions1.width();
        double entity2Volume = entityDimensions.height() * entityDimensions.width() * entityDimensions.width();
        return entity1Volume / entity2Volume;
    }

    public static void onPushed(LivingEntity pusher, Entity pushed) {
        if (!pushed.isPassenger() && pushed instanceof LivingEntity livingPushed) {
            double ratio = getRatio(pusher, pushed);
            if (ratio >= Server.TRAMPLE_RATIO.get()) {
                livingPushed.hurt(livingPushed.damageSources().cramming(), 2);
            }
        }
    }

    public static Vec3 getRideVector(Mob mob, Player player) {
        if (mob instanceof Parrot) {
            return new Vec3(player.xxa, 0, player.zza);
        }
        return null;
    }

    public static LivingEntity forcePlayerControl(Mob mob) {
        Entity passenger = mob.getFirstPassenger();
        if (passenger instanceof Player player && mob instanceof Parrot) {
            return player;
        }
        return null;
    }

    public static void onTickRidden(LivingEntity livingEntity, Player player, Vec3 pTravelVector) {
        Vec2 vec2 = new Vec2(player.getXRot() * 0.5F, player.getYRot());
        //livingEntity.setRot(vec2.y, vec2.x);
        livingEntity.setYRot(vec2.y % 360.0F);
        livingEntity.setXRot(vec2.x % 360.0F);
        livingEntity.yRotO = livingEntity.yBodyRot = livingEntity.yHeadRot = livingEntity.getYRot();

        if (livingEntity instanceof Parrot parrot) {

            boolean jumping = player.jumping;
            if (jumping) {
                parrot.addDeltaMovement(new Vec3(0, 0.1, 0));
            }

            // parrot.getJumpControl().jump();
            //   parrot.getNavigation() .moveTo(0,0,0,1);
        }
    }

    public static boolean isBeingHeldByGulliver(LivingEntity entity,Entity passenger) {
        LivingEntityDuck duck = LivingEntityDuck.of(entity);
        for (Entity entity1 : duck.getMountPositions().values()) {
            if (entity1 == passenger) return true;
        }
        return false;
    }

    public static void onLivingTick(LivingEntity living) {
        LivingEntityDuck duck = LivingEntityDuck.of(living);
        for (Map.Entry<MountPosition,Entity> entry : duck.getMountPositions().entrySet()) {
            MountPosition mountPosition = entry.getKey();
            Entity entity = entry.getValue();

            if (entity.getVehicle() != living) {
                LOG.error("Desync between {} and {} in position {} detected, correcting",living,entity,mountPosition);
                duck.removeMount(mountPosition);
            }

            if (!canRide(living,entity)) {
                entity.stopRiding();
                duck.removeMount(mountPosition);
                S2CRemoveMountPositionPacket.sendToTracking(living, mountPosition);
            }
        }

        List<Entity> passengers = new ArrayList<>(living.getPassengers());

        for (Entity entity : passengers) {
            if (!onAttemptRide(entity,living)) {
                entity.stopRiding();
            }
        }
    }

    public static InteractionResult entityInteract(Player player, Level world, InteractionHand hand, Entity entity) {
        if (canPickup(player, hand, entity)) {
            boolean worked = entity.startRiding(player);
            if (!worked) {
                return InteractionResult.PASS;
            }
            LivingEntityDuck playerDuck = LivingEntityDuck.of(player);
            MountPosition mountPosition = getPosition(player,hand);

            playerDuck.addMount(mountPosition, entity);

            if (entity instanceof ServerPlayer playerPassenger) {
                ModCriteriaTriggers.PICKED_UP.trigger(playerPassenger);
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            if (entity instanceof LivingEntity livingVehicle) {
                if (canRide(livingVehicle, player)) {
                    if (livingVehicle instanceof Parrot parrot) {
                        if (parrot.isTame() && player.getUUID().equals(parrot.getOwnerUUID())) {
                            if (!world.isClientSide) {
                                parrot.setOrderedToSit(false);
                                parrot.setInSittingPose(false);
                                player.startRiding(parrot);
                                parrot.setNoGravity(true);
                            }
                            return InteractionResult.sidedSuccess(world.isClientSide);
                        }
                    } else if (player.getMainHandItem().is(Items.STRING)) {
                        if (canRide(livingVehicle, player)) {
                            if (!world.isClientSide) {
                                player.startRiding(livingVehicle);
                                LivingEntityDuck.of(livingVehicle).addMount(MountPosition.TOP, player);
                                S2CSetMountPositionPacket.sendToTracking(livingVehicle, MountPosition.TOP, player);
                                ModCriteriaTriggers.FORCE_RIDE.trigger((ServerPlayer) player);
                            }
                            return InteractionResult.sidedSuccess(world.isClientSide);
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static void onStopRiding(Entity passenger, Entity vehicle) {
        if (vehicle instanceof LivingEntity livingVehicle) {
            LivingEntityDuck duck = LivingEntityDuck.of(livingVehicle);
            Map<MountPosition, Entity> mountPositions = duck.getMountPositions();

            MountPosition mountPosition = null;
            for (Map.Entry<MountPosition, Entity> entry : mountPositions.entrySet()) {
                if (entry.getValue() == passenger) {
                    mountPosition = entry.getKey();
                    break;
                }
            }
            if (mountPosition != null) {
                duck.removeMount(mountPosition);
                if (vehicle instanceof ServerPlayer player) {
                    S2CDropEntityPacket.sendTo(player,mountPosition);
                }
            }
        }
    }

    public static boolean onAttemptRide(Entity entity, Entity vehicle) {
        double scale0 = getScale(entity);
        double scale1 = getScale(vehicle);
        boolean b = scale0 / scale1 < Server.MAX_RIDING_RATIO.get();
        if (!b && entity instanceof ServerPlayer player) {
            player.displayClientMessage(Component.literal("Too big to ride"),true);
        }

        return b;
    }

    public static double getScale(Entity entity) {
        ScaleData scaleData = ScaleTypes.BASE.getScaleData(entity);
        return scaleData.getScale();
    }

    public static double changeClimbingSpeed(LivingEntity livingEntity, double constant) {
        return constant *4;
    }
}