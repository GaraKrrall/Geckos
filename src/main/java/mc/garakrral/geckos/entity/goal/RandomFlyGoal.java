/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;


/**
 * Simple AI goal that occasionally sends a flying mob toward a random nearby position.
 */
@Deprecated(forRemoval = true)
public class RandomFlyGoal extends Goal {
    private final PathfinderMob entity;
    private final double speed;
    private final int i;

    /**
     * Creates the goal.
     *
     * @param entity mob controlled by this goal
     * @param s movement speed passed to the navigation system
     * @param i inverse probability divisor controlling how often the goal starts
     */
    public RandomFlyGoal(PathfinderMob entity, double s, int i) {
        this.entity = entity;
        this.speed = s;
        this.i = i;
    }

    /**
     * Continues while the mob still has an active navigation target.
     *
     * @return {@code true} if navigation is still in progress
     */
    @Override
    public boolean canContinueToUse() {
        return !entity.getNavigation().isDone();
    }

    /**
     * Randomly decides whether the goal should start this tick.
     *
     * @return {@code true} when the random interval check succeeds
     */
    @Override
    public boolean canUse() {
        return entity.getRandom().nextInt(i) == 0;
    }

    /**
     * Selects a random nearby destination and asks navigation to move toward it.
     */
    @Override
    public void start() {
        RandomSource random = entity.getRandom();

        double x = entity.getX() + (random.nextDouble() * 10.0 - 5.0);
        double y = entity.getY() + (random.nextDouble() * 4.0 - 2.0);
        double z = entity.getZ() + (random.nextDouble() * 10.0 - 5.0);

        entity.getNavigation().moveTo(x, y, z , speed);
    }
}
