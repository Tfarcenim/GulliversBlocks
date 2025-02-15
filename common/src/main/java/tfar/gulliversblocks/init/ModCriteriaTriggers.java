package tfar.gulliversblocks.init;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.advancements.ForceRideTrigger;
import tfar.gulliversblocks.advancements.PickedUpTrigger;
import tfar.gulliversblocks.advancements.ReachSizeTrigger;

public class ModCriteriaTriggers {

    public static final ReachSizeTrigger REACH_SIZE = register("reach_size",new ReachSizeTrigger());
    public static final PickedUpTrigger PICKED_UP = register("picked_up",new PickedUpTrigger());
    public static final ForceRideTrigger FORCE_RIDE = register("force_ride",new ForceRideTrigger());


    public static void boot() {}

    public static <T extends CriterionTrigger<?>> T register(String pName, T pTrigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, GulliversBlocks.id(pName), pTrigger);
    }


}
