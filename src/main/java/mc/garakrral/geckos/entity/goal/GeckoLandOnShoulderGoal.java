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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;

public class GeckoLandOnShoulderGoal extends Goal {
    private final GeckoEntity entity;
    private ServerPlayer owner;
    private boolean mounted;

    public GeckoLandOnShoulderGoal(GeckoEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (!(entity.getOwner() instanceof ServerPlayer player)) return false;

        boolean valid = !player.isSpectator()
                && !player.getAbilities().flying
                && !player.isInWater()
                && !player.isInPowderSnow;

        return !entity.isOrderedToSit()
                && valid
                && entity.canSitOnShoulder()
                && !entity.isBaby();
    }

    @Override
    public boolean isInterruptable() {
        return !mounted;
    }

    @Override
    public void start() {
        owner = (ServerPlayer) entity.getOwner();
        mounted = false;
    }

    @Override
    public void tick() {
        if (mounted || entity.isInSittingPose() || entity.isLeashed()) return;

        if (entity.getBoundingBox().intersects(owner.getBoundingBox())) {
            mounted = entity.setEntityOnShoulder(owner);
        }
    }
}