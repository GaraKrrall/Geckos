package mc.garakrral.geckos.entity.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class RandomFlyGoal extends Goal {
    private final PathfinderMob entity;
    private final double speed;
    private final int i;

    public RandomFlyGoal(PathfinderMob entity, double s, int i) {
        this.entity = entity;
        this.speed = s;
        this.i = i;
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.getNavigation().isDone();
    }

    @Override
    public boolean canUse() {
        return entity.getRandom().nextInt(i) == 0;
    }

    @Override
    public void start() {
        RandomSource random = entity.getRandom();

        double x = entity.getX() + (random.nextDouble() * 10.0 - 5.0);
        double y = entity.getY() + (random.nextDouble() * 4.0 - 2.0);
        double z = entity.getZ() + (random.nextDouble() * 10.0 - 5.0);

        entity.getNavigation().moveTo(x, y, z , speed);
    }
}
