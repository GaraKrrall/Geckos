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

public class ServerParticlesEvent {
    public static void createHeartParticles(GeckoEntity entity, int count, double speed) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + 1, entity.getZ(), count, 0.3, 0.3, 0.3, speed);
        }
    }
}
