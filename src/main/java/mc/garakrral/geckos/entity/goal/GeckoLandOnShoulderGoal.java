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

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * AI goal that lets a tamed adult gecko mount its owner's shoulder.
 */
public class GeckoLandOnShoulderGoal extends Goal {
    private final GeckoEntity entity;
    private ServerPlayer owner;
    private boolean mounted;

    /**
     * Creates the shoulder-mount goal.
     *
     * @param entity gecko controlled by this goal
     */
    public GeckoLandOnShoulderGoal(GeckoEntity entity) {
        this.entity = entity;
    }

    /**
     * Checks whether the gecko is currently allowed to attempt shoulder mounting.
     *
     * @return {@code true} when the gecko has a valid owner and satisfies all mount conditions
     */
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

    /**
     * Prevents interruption after mounting has succeeded.
     *
     * @return {@code true} until the gecko has actually mounted
     */
    @Override
    public boolean isInterruptable() {
        return !mounted;
    }

    /**
     * Initializes cached state before the goal starts ticking.
     */
    @Override
    public void start() {
        owner = (ServerPlayer) entity.getOwner();
        mounted = false;
    }

    /**
     * Attempts to transfer the gecko onto the owner's shoulder when they intersect.
     */
    @Override
    public void tick() {
        if (mounted || entity.isInSittingPose() || entity.isLeashed()) return;

        if (entity.getBoundingBox().intersects(owner.getBoundingBox())) {
            mounted = entity.setEntityOnShoulder(owner);
        }
    }
}
