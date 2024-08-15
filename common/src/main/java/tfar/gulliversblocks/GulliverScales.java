package tfar.gulliversblocks;

import it.unimi.dsi.fastutil.ints.Int2DoubleLinkedOpenHashMap;

public class GulliverScales {

    public static final Int2DoubleLinkedOpenHashMap SCALES = new Int2DoubleLinkedOpenHashMap();

    public static void addScales() {
        SCALES.clear();

        SCALES.put(-7,.03125);
        SCALES.put(-6,.0625);
        SCALES.put(-5,.125);
        SCALES.put(-4,.25);
        SCALES.put(-3,.5);
        SCALES.put(-2,.75);
        SCALES.put(-1,.875);

        SCALES.put(0,1);

        SCALES.put(1,1.25);
        SCALES.put(2,1.5);
        SCALES.put(3,5);
        SCALES.put(4,6.75);
        SCALES.put(5,9);
        SCALES.put(6,12);
        SCALES.put(7,16);

    }

    public static boolean valid(int gulliverScale) {
        return gulliverScale >= SCALES.firstIntKey() && gulliverScale <= SCALES.lastIntKey();
    }

    public static int min() {
        return SCALES.firstIntKey();
    }

    public static int max() {
        return SCALES.lastIntKey();
    }

}
//Small 0.03125x (0.0625), 0.625 (0.125), 0.125 (0.25), 0.25x (0.5), 0.5x (1), 0.75x (1.50), 0.875x (1.75)
//
//Default 1x (2 meters/blocks)
//
//Large 1.25x (2.5), 1.5x (3), 5x (10), 6.75x (13.5), 9x (18), 12x (24), 16x (32)