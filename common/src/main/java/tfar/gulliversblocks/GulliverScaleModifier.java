package tfar.gulliversblocks;

import tfar.gulliversblocks.config.GulliversBlocksConfig;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleTypes;

public class GulliverScaleModifier extends ScaleModifier {

    public GulliverScaleModifier() {
        super(256);
    }

    @Override
    public float modifyScale(ScaleData scaleData, float modifiedScale, float delta) {
        if (scaleData.getScaleType() == ScaleTypes.MOTION) {
            return (float) GulliversBlocksConfig.Server.THROWN_POTION_MOTION_SCALING.get().function.get(modifiedScale);
        }
        return modifiedScale;
    }

    @Override
    public float modifyPrevScale(ScaleData scaleData, float modifiedScale) {
        if (scaleData.getScaleType() == ScaleTypes.MOTION) {
        }
        return modifiedScale;
    }
}
