/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.event;

import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

/**
 * Server-side particle helpers used by gameplay events.
 *
 * <p>The methods in this class intentionally operate on {@link ServerLevel} so particle spawning is
 * authoritative and automatically synchronized to nearby clients by the server.
 */
public class ServerParticlesEvent {
    /**
     * Spawns heart particles around the supplied gecko entity.
     *
     * @param entity gecko entity that should emit the particles
     * @param count number of particle instances to request
     * @param speed particle motion speed value passed to the server particle API
     */
    public static void createHeartParticles(GeckoEntity entity, int count, double speed) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + 1, entity.getZ(), count, 0.3, 0.3, 0.3, speed);
        }
    }
}
