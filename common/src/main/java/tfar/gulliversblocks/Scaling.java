package tfar.gulliversblocks;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraft.util.Mth;

public enum Scaling {
    NONE(d -> 1),
    LINEAR(d -> d),
    SQUARE(d -> d*d),
    CUBE(d -> d*d*d),
    SQUARE_ROOT(Math::sqrt),
    INVERSE(d -> 1/d),
    INVERSE_SQUARE_ROOT(Mth::fastInvSqrt),
    INVERSE_CUBE_ROOT(d -> Math.pow(d,-1/3d)),
    INVERSE_CUBE_ROOT_ABOVE_NONE_BELOW(d -> d < 1 ? 1 : Math.pow(d,-1/3d)),
    INVERSE_SQUARE_ROOT_ABOVE_INVERSE_THREE_QUARTERS_BELOW(d -> d < 1 ? Math.pow(d,-.75) : Mth.fastInvSqrt(d)),
    //INVERSE_CUBE_ROOT_ABOVE_INVERSE_SQUARE_ROOT_BELOW(d -> d < 1 ? Math.pow(d,-.33) : Math.pow(d,-1/3d))

    ;
    public final Double2DoubleFunction function;

    Scaling(Double2DoubleFunction function) {
        this.function = function;
    }
}
