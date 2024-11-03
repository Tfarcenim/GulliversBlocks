package tfar.gulliversblocks.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

public class LivingWaterCallbacks {

    public static final Event<Breathing> BREATHING = EventFactory.createArrayBacked(Breathing.class, callbacks -> (LivingEntity entity, BreathResult result) -> {
        for (Breathing callback : callbacks) callback.onBreath(entity, result);
    });

    public static final Event<Drowning> DROWNING = EventFactory.createArrayBacked(Drowning.class,callbacks -> (entity, result) -> {
        for (Drowning callback : callbacks) callback.onDrown(entity,result);
    });

    public static class BreathResult {
        private boolean canBreathe;
        private int consumeAirAmount;
        private int refillAirAmount;

        public BreathResult(boolean canBreathe, int consumeAirAmount, int refillAirAmount) {
            this.canBreathe = canBreathe;
            this.consumeAirAmount = consumeAirAmount;
            this.refillAirAmount = refillAirAmount;
        }

        public boolean isCanBreathe() {
            return canBreathe;
        }

        public int getConsumeAirAmount() {
            return consumeAirAmount;
        }

        public int getRefillAirAmount() {
            return refillAirAmount;
        }

        public void setCanBreathe(boolean canBreathe) {
            this.canBreathe = canBreathe;
        }

        public void setConsumeAirAmount(int consumeAirAmount) {
            this.consumeAirAmount = consumeAirAmount;
        }

        public void setRefillAirAmount(int refillAirAmount) {
            this.refillAirAmount = refillAirAmount;
        }
    }

    @FunctionalInterface
    public interface Breathing {
        void onBreath(LivingEntity entity, BreathResult result);
    }

    @FunctionalInterface
    public interface Drowning {
        void onDrown(LivingEntity entity, DrownResult result);
    }

    public static class DrownResult {
        private boolean isDrowning;
        private float damageAmount;
        private int bubbleCount;

        public DrownResult(boolean isDrowning, float damageAmount, int bubbleCount) {
            this.isDrowning = isDrowning;
            this.damageAmount = damageAmount;
            this.bubbleCount = bubbleCount;
        }

        public boolean isDrowning() {
            return isDrowning;
        }

        public void setDrowning(boolean drowning) {
            isDrowning = drowning;
        }

        public float getDamageAmount() {
            return damageAmount;
        }

        public void setDamageAmount(float damageAmount) {
            this.damageAmount = damageAmount;
        }

        public int getBubbleCount() {
            return bubbleCount;
        }

        public void setBubbleCount(int bubbleCount) {
            this.bubbleCount = bubbleCount;
        }
    }

}
