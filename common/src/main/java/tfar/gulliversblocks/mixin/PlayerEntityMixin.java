package tfar.gulliversblocks.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import tfar.gulliversblocks.GulliversBlocks;
import tfar.gulliversblocks.MountPosition;
import tfar.gulliversblocks.PlayerDuck;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerDuck {

    Map<MountPosition,Entity> mountPositions = new EnumMap<>(MountPosition.class);

    @Shadow public abstract void travel(Vec3 pTravelVector);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction callback) {
        super.positionRider(passenger, callback);
    }

    @Override
    public Map<MountPosition, Entity> getMountPositions() {
        return mountPositions;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity pEntity, EntityDimensions pDimensions, float pPartialTick) {
        MountPosition mountPos = null;
        for (Map.Entry<MountPosition,Entity> entry: mountPositions.entrySet()) {
            if (entry.getValue() == pEntity) {
                mountPos = entry.getKey();
            }
        }

        if (mountPos == null) {
            return super.getPassengerAttachmentPoint(pEntity, pDimensions, pPartialTick);
        }

        return GulliversBlocks.repositionRiders((Player)(Object)this,pEntity,pDimensions,pPartialTick,mountPos);
    }

}
