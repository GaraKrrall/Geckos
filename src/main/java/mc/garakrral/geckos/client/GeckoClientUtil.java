/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client;

import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoClientUtil {

    public static void clientCarryPreview(GeckoEntity gecko) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        if (gecko.getOwner() == null || !gecko.getOwner().equals(player)) return;

        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();

        double minDist = player.getBbWidth() + gecko.getBbWidth() + 0.35D;
        double wantedDist = Mth.clamp(gecko.getCarryDistance(), minDist, GeckoEntity.MAX_CARRY_DISTANCE);

        Vec3 target = eyePos.add(look.scale(wantedDist));

        HitResult hit = gecko.level().clip(
                new ClipContext(eyePos, target, ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE, player)
        );

        if (hit.getType() != HitResult.Type.MISS) {
            target = hit.getLocation().subtract(look.scale(gecko.getBbWidth() + 0.25D));
        }

        double y = target.y - gecko.getBbHeight() / 2D;

        gecko.setPos(target.x, y, target.z);
        gecko.setDeltaMovement(Vec3.ZERO);
    }
}