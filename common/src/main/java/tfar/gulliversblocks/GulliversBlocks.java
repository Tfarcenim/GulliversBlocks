package tfar.gulliversblocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.gulliversblocks.init.ModMobEffects;
import tfar.gulliversblocks.init.ModPotions;
import virtuoel.pehkui.api.*;

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

    public static void onGulliverScaleChange(LivingEntity living,int oldScale, int newScale) {
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
                GulliversBlocks.LOG.warn("Tried to set gulliver scale out of bounds {}",newScale);
            }
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID,path);
    }

}