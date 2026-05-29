package mc.garakrral.geckos.entity.custom.base;

import mc.garakrral.geckos.entity.variant.type.GeckoType;
import mc.garakrral.geckos.entity.variant.type.GeckoTypeHelper;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.level.Level;

public abstract class BaseGeckoEntity extends ShoulderRidingEntity implements GeckoTypeHelper {
    protected GeckoType type;

    protected BaseGeckoEntity(EntityType<? extends ShoulderRidingEntity> entityType, Level level, GeckoType type) {
        super(entityType, level);
        this.type = type;
    }

    @Override
    public GeckoType getGeckoType() {
        return type;
    }

}
