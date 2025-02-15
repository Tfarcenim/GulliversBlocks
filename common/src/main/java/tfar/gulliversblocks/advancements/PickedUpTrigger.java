package tfar.gulliversblocks.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import tfar.gulliversblocks.init.ModCriteriaTriggers;

import java.util.Optional;

public class PickedUpTrigger extends SimpleCriterionTrigger<PickedUpTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer pPlayer) {
        this.trigger(pPlayer, p_160394_ -> true);
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                p_337396_ -> p_337396_.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player))
                        .apply(p_337396_, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> playerPickedUp(EntityPredicate.Builder pPlayer) {
            return ModCriteriaTriggers.PICKED_UP.createCriterion(new TriggerInstance(Optional.of(EntityPredicate.wrap(pPlayer))));
        }
    }
}

