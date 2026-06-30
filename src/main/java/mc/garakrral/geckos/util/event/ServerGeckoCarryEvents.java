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

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Authoritative carry-position logic for geckos manipulated by players.
 *
 * <p>This code runs on the logical server and computes the desired carried position from the owning
 * player's eye position, look vector, configured carry distance, and world collisions. The result is
 * the canonical position used by gameplay systems and synchronized back to clients through normal
 * entity updates.
 */
public class ServerGeckoCarryEvents {

    /**
     * Recomputes and applies the world position of a carried gecko.
     *
     * @param gecko carried gecko whose owner, target distance, and collision constraints should be
     *              evaluated
     */
    public static void updateCarriedServerPosition(GeckoEntity gecko) {
        var geckoOwner = gecko.getOwner();

        if (!(geckoOwner instanceof Player player) || !player.isAlive()) {
            gecko.setCarried(false);
            return;
        }

        gecko.setShoulderRiding(false);
        gecko.getNavigation().stop();
        gecko.setTarget(null);
        gecko.setDeltaMovement(Vec3.ZERO);
        gecko.fallDistance = 0;
        gecko.setNoAi(true);

        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();

        double minDist = player.getBbWidth() + gecko.getBbWidth() + 0.35D;
        double wantedDist = Mth.clamp(gecko.getCarryDistance(), minDist, GeckoEntity.MAX_CARRY_DISTANCE);

        Vec3 target = eyePos.add(look.scale(wantedDist));
        HitResult hit = gecko.level().clip(new ClipContext(eyePos, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hit.getType() != HitResult.Type.MISS) {
            target = hit.getLocation().subtract(look.scale(gecko.getBbWidth() + 0.25D));
        }

        double y = target.y - gecko.getBbHeight() / 2D;
        Vec3 finalPos = new Vec3(target.x, y, target.z);
        AABB box = gecko.getBoundingBox().move(finalPos.subtract(gecko.position()));

        if (!gecko.level().noCollision(gecko, box)) {
            return;
        }

        gecko.moveTo(finalPos.x, finalPos.y, finalPos.z, player.getYRot(), player.getXRot());
    }
}
