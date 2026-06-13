/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.goal;

import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.TamableAnimal;

/**
 * Follow-owner goal variant that respects the gecko's custom sitting state.
 */
public class GeckoFollowOwnerGoal extends FollowOwnerGoal {

    private final TamableAnimal tamable;

    /**
     * Creates the goal.
     *
     * @param animal tamable animal that should follow its owner
     * @param speed movement speed
     * @param start distance at which following begins
     * @param stop distance at which following stops
     */
    public GeckoFollowOwnerGoal(TamableAnimal animal, double speed, float start, float stop) {
        super(animal, speed, start, stop);
        this.tamable = animal;
    }

    /**
     * Blocks follow behavior while the gecko is explicitly sitting.
     *
     * @return {@code true} when the base goal can run and the gecko is not sitting
     */
    @Override
    public boolean canUse() {
        if (tamable instanceof GeckoEntity gecko && gecko.isSittingGecko()) {
            return false;
        }
        return super.canUse();
    }

    /**
     * Stops continuing follow behavior when the gecko has been told to sit.
     *
     * @return {@code true} when continuation remains valid
     */
    @Override
    public boolean canContinueToUse() {
        if (tamable instanceof GeckoEntity gecko && gecko.isSittingGecko()) {
            return false;
        }
        return super.canContinueToUse();
    }

    /**
     * Skips follow updates while the gecko is sitting and otherwise delegates to the base goal.
     */
    @Override
    public void tick() {
        if (tamable instanceof GeckoEntity gecko && gecko.isSittingGecko()) {
            return;
        }

        super.tick();
    }
}
