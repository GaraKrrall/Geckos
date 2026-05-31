/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.goal;

import mc.garakrral.geckos.entity.custom.GeckoEntity;

import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.TamableAnimal;

public class GeckoFollowOwnerGoal extends FollowOwnerGoal {

    private final TamableAnimal tamable;

    public GeckoFollowOwnerGoal(TamableAnimal animal, double speed, float start, float stop) {
        super(animal, speed, start, stop);
        this.tamable = animal;
    }

    @Override
    public boolean canUse() {
        if (tamable instanceof GeckoEntity gecko && gecko.isSittingGecko()) {
            return false;
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        if (tamable instanceof GeckoEntity gecko && gecko.isSittingGecko()) {
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        if (tamable instanceof GeckoEntity gecko && gecko.isSittingGecko()) {
            return;
        }

        super.tick();
    }
}