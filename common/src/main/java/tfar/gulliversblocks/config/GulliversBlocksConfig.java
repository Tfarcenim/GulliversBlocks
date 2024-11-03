package tfar.gulliversblocks.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import tfar.gulliversblocks.Scaling;

public class GulliversBlocksConfig {

    public static final Server SERVER;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        final Pair<Server,ModConfigSpec> specPair2 = new ModConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }


    public static class Server {

        public static ModConfigSpec.ConfigValue<Scaling> BLOCK_BREAK_SPEED_SCALING;
        public static ModConfigSpec.ConfigValue<Scaling> MAX_HEALTH_SCALING;
        public static ModConfigSpec.DoubleValue MINIMUM_MAX_HEALTH_SCALE;
        public static ModConfigSpec.ConfigValue<Scaling> ATTACK_DAMAGE_SCALING;

        public Server(ModConfigSpec.Builder builder) {
            builder.push("scaling");
            BLOCK_BREAK_SPEED_SCALING = builder.defineEnum("player.block_break_speed",Scaling.SQUARE_ROOT);
            MAX_HEALTH_SCALING = builder.defineEnum("generic.max_health",Scaling.LINEAR);
            MINIMUM_MAX_HEALTH_SCALE = builder.defineInRange("minimum_max_health_scale",.5,0,1);
            ATTACK_DAMAGE_SCALING = builder.defineEnum("generic.attack_damage",Scaling.SQUARE_ROOT);
            builder.pop();
        }
    }

}
