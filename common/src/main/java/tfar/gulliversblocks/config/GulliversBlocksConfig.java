package tfar.gulliversblocks.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import it.unimi.dsi.fastutil.ints.Int2DoubleLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import tfar.gulliversblocks.GulliverScales;
import tfar.gulliversblocks.Scaling;

import java.util.HashMap;
import java.util.Map;

public class GulliversBlocksConfig {

    public static final Server SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        final Pair<Server,ModConfigSpec> specPair2 = new ModConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }


    //Small ¼ a block, ½ a block
    //Default 2 (meters/blocks)
    //Large 4, 16

    public static Int2DoubleMap defaults() {
        Int2DoubleMap map = new Int2DoubleLinkedOpenHashMap();
        map.put(-2,1/8f);
        map.put(-1,1/4f);
        map.put(0,1);
        map.put(1,2);
        map.put(2,4);
        return map;
    }

    public static class Server {

        public static ModConfigSpec.ConfigValue<Scaling> BLOCK_BREAK_SPEED_SCALING;
        public static ModConfigSpec.ConfigValue<Scaling> MAX_HEALTH_SCALING;
        public static ModConfigSpec.DoubleValue MINIMUM_MAX_HEALTH_SCALE;
        public static ModConfigSpec.ConfigValue<Scaling> ATTACK_DAMAGE_SCALING;
        public static ModConfigSpec.ConfigValue<Scaling> MOVEMENT_SPEED_SCALING;
        public static ModConfigSpec.ConfigValue<Scaling> FALL_DAMAGE_MULTIPLIER_SCALING;
        public static ModConfigSpec.ConfigValue<Scaling> SAFE_FALL_DISTANCE_SCALING;

        public static ConfigHelper.ConfigObject<Map<Integer,Double>> SCALES;


        public Server(ModConfigSpec.Builder builder) {
            builder.push("scaling");

            SCALES = ConfigHelper.defineObject(builder, "scales",GulliverScales.CODEC,defaults());

            BLOCK_BREAK_SPEED_SCALING = builder.defineEnum("player.block_break_speed",Scaling.SQUARE_ROOT);
            MAX_HEALTH_SCALING = builder.defineEnum("generic.max_health",Scaling.LINEAR);
            MINIMUM_MAX_HEALTH_SCALE = builder.defineInRange("minimum_max_health_scale",.5,0,1);
            ATTACK_DAMAGE_SCALING = builder.defineEnum("generic.attack_damage",Scaling.SQUARE_ROOT);
            MOVEMENT_SPEED_SCALING = builder.defineEnum("generic.movement_speed",Scaling.INVERSE_SQUARE_ROOT);
            FALL_DAMAGE_MULTIPLIER_SCALING = builder.defineEnum("generic.fall_damage_multiplier",Scaling.LINEAR);
            SAFE_FALL_DISTANCE_SCALING = builder.defineEnum("generic.safe_fall_distance",Scaling.INVERSE_SQUARE_ROOT);
            builder.pop();
        }
    }
}
