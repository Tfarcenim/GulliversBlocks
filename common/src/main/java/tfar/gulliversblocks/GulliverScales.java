package tfar.gulliversblocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.UnboundedMapCodec;
import tfar.gulliversblocks.config.GulliversBlocksConfig;

public class GulliverScales {


    public static final UnboundedMapCodec<Integer, Double> CODEC = Codec.unboundedMap(Codec.INT,Codec.DOUBLE);


    public static boolean valid(int gulliverScale) {
        return GulliversBlocksConfig.Server.SCALES.get().containsKey(gulliverScale);
    }

}
//Small 0.03125x (0.0625), 0.625 (0.125), 0.125 (0.25), 0.25x (0.5), 0.5x (1), 0.75x (1.50), 0.875x (1.75)
//
//Default 1x (2 meters/blocks)
//
//Large 1.25x (2.5), 1.5x (3), 5x (10), 6.75x (13.5), 9x (18), 12x (24), 16x (32)