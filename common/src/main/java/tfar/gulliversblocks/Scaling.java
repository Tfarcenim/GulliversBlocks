package tfar.gulliversblocks;

import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;

public enum Scaling {
    NONE(d -> 1),
    LINEAR(d -> d),
    SQUARE(d -> d*d),
    CUBE(d -> d*d*d),
    SQUARE_ROOT(Math::sqrt);
    public final Double2DoubleFunction function;

    Scaling(Double2DoubleFunction function) {
        this.function = function;
    }
}
