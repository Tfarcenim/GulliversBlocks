package tfar.gulliversblocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import tfar.gulliversblocks.init.ModCriteriaTriggers;

import java.util.Optional;

public class ReachSizeTrigger extends SimpleCriterionTrigger<ReachSizeTrigger.TriggerInstance> {

    public void trigger(ServerPlayer pPlayer,double newSize) {
        this.trigger(pPlayer,triggerInstance -> triggerInstance.matches(newSize));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,MinMaxBounds.Doubles size) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                                MinMaxBounds.Doubles.CODEC
                                        .optionalFieldOf("size", MinMaxBounds.Doubles.atLeast(0))
                                        .forGetter(TriggerInstance::size)
                        )
                        .apply(instance, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> reachSize(MinMaxBounds.Doubles targetSize) {
            return ModCriteriaTriggers.REACH_SIZE_TRIGGER.createCriterion(new TriggerInstance(Optional.empty(), targetSize));
        }

        public boolean matches(double scale) {
            return size.matches(scale);
        }
    }
}
