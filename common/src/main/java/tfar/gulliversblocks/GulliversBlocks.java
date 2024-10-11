package tfar.gulliversblocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.gulliversblocks.init.ModMobEffects;
import tfar.gulliversblocks.init.ModPotions;
import tfar.gulliversblocks.network.client.S2CRemoveMountPositionPacket;
import tfar.gulliversblocks.network.client.S2CSetMountPositionPacket;
import tfar.gulliversblocks.platform.Services;
import virtuoel.pehkui.api.*;

import java.util.Map;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class GulliversBlocks {

    public static final String MOD_ID = "gulliversblocks";
    public static final String MOD_NAME = "Gulliver's Blocks";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.

        GulliverScales.addScales();


    }

    public static void register() {
        ModMobEffects.boot();
        ModPotions.boot();
    }

    public static void onGulliverScaleChange(LivingEntity living, int oldScale, int newScale) {
        if (newScale == 0) {
            for (ScaleType type : ScaleRegistries.SCALE_TYPES.values()) {
                ScaleData data = type.getScaleData(living);
                Boolean persist = data.getPersistence();
                data.resetScale();
                data.setPersistence(persist);
            }
        } else {

            if (GulliverScales.valid(newScale)) {
                double gulliverScale = GulliverScales.SCALES.get(newScale);
                ScaleData scaleData = ScaleTypes.BASE.getScaleData(living);
                scaleData.setScaleTickDelay(40);
                scaleData.setPersistence(true);
                scaleData.setTargetScale((float) gulliverScale);
            } else {
                GulliversBlocks.LOG.warn("Tried to set gulliver scale out of bounds {}", newScale);
            }
        }
    }

    public static Vec3 repositionRiders(Player player, Entity pEntity, EntityDimensions pDimensions, float pPartialTick,MountPosition mountPosition) {

        switch (mountPosition) {
            case LEFT_SHOULDER -> {
                float z = 0.475f * pDimensions.width();
                int j = mountPosition == MountPosition.LEFT_SHOULDER ? 1 : -1;
                float x = j * .575f * pDimensions.width();
                float y = .875f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-player.yBodyRot * (float) (Math.PI / 180.0));
            }
            case RIGHT_SHOULDER -> {
                float z = 0 * pDimensions.width();
                int j = mountPosition == MountPosition.LEFT_SHOULDER ? 1 : -1;
                float x = j * .600f * pDimensions.width();
                float y = .8f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-player.yBodyRot * (float) (Math.PI / 180.0));
            }
            case LEFT_HAND -> {
                float z = 0.475f * pDimensions.width();
                int j = 1;//
                float x = j * .575f * pDimensions.width();
                float y = .375f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-player.yBodyRot * (float) (Math.PI / 180.0));
            }
            case RIGHT_HAND -> {
                float z = 0.475f * pDimensions.width();
                int j = -1;//
                float x = j * .575f * pDimensions.width();
                float y = .375f;

                return new Vec3(x, pDimensions.height() * y, z)
                        .yRot(-player.yBodyRot * (float) (Math.PI / 180.0));
            }
        };
        throw new RuntimeException("Unexpected mountpos:" +mountPosition);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    static boolean canPickup(Player player, InteractionHand hand, Entity entity) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        HumanoidArm mainArm = player.getMainArm();
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

        EntityDimensions playerDimensions = player.getDimensions(player.getPose());
        EntityDimensions entityDimensions = entity.getDimensions(entity.getPose());
        double playerVolume = playerDimensions.height() * playerDimensions.width() * playerDimensions.width();
        double entityVolume = entityDimensions.height() * entityDimensions.width() * entityDimensions.width();
        double ratio = playerVolume / entityVolume;

        return ratio >= 6;
    }

    public static void swap(ServerPlayer player, MountPosition pos1, MountPosition pos2) {
        PlayerDuck playerDuck = PlayerDuck.of(player);
        Map<MountPosition, Entity> mounts = playerDuck.getMountPositions();
        Entity mount1 = mounts.get(pos1);
        Entity mount2 = mounts.get(pos2);

        if (mount2 != null) {
            mounts.put(pos1, mount2);
            Services.PLATFORM.sendToClient(new S2CSetMountPositionPacket(pos1,mount2),player);
        } else {
            mounts.remove(pos1);
            Services.PLATFORM.sendToClient(new S2CRemoveMountPositionPacket(pos1),player);
        }

        if (mount1 != null) {
            mounts.put(pos2, mount1);
            Services.PLATFORM.sendToClient(new S2CSetMountPositionPacket(pos2,mount1),player);
        } else {
            mounts.remove(pos2);
            Services.PLATFORM.sendToClient(new S2CRemoveMountPositionPacket(pos2),player);
        }
    }
}