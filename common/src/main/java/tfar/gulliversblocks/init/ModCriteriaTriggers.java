package tfar.gulliversblocks.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.ReachSizeTrigger;

public class ModCriteriaTriggers {

    public static final ReachSizeTrigger REACH_SIZE_TRIGGER = register("reach_size_trigger",new ReachSizeTrigger());


    public static void boot() {}

    public static <T extends CriterionTrigger<?>> T register(String pName, T pTrigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, GulliversBlocks.id(pName), pTrigger);
    }


}
