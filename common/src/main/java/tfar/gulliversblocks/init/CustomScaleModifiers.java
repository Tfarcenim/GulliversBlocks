package tfar.gulliversblocks.init;

import net.minecraft.resources.ResourceLocation;
import tfar.gulliversblocks.GulliverScaleModifier;
import tfar.gulliversblocks.GulliversBlocks;
import virtuoel.pehkui.api.*;

public class CustomScaleModifiers {

    public static final ScaleModifier CUSTOM_MOTION = register("custom_motion", new GulliverScaleModifier());

    private static ScaleModifier register(String path, ScaleModifier scaleModifier)
    {
        return register(GulliversBlocks.id(path), scaleModifier);
    }

    private static ScaleModifier register(ResourceLocation id, ScaleModifier scaleModifier)
    {
        return ScaleRegistries.register(
                ScaleRegistries.SCALE_MODIFIERS,
                id,
                scaleModifier
        );
    }

    private static ScaleModifier register(ResourceLocation id)
    {
        return register(id, new ScaleModifier());
    }

    public static void init() {

    }

}
