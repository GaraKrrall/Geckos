/*
 * Copyright (c) 2026 GaraKrral
 *
 * Source code in this project is licensed under the GNU General Public License v3.0 (GPLv3).
 * See the LICENSE file for details.
 *
 * All game assets, including but not limited to graphics, audio, models, textures,
 * and other non-code content, are proprietary and All Rights Reserved unless
 * explicitly stated otherwise.
 *
 */

package mc.garakrral.geckos.entity.goal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlyWanderGoal extends Goal {

    private final PathfinderMob mob;

    public FlyWanderGoal(PathfinderMob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getNavigation().isDone()
                && mob.getRandom().nextInt(8) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        Vec3 pos = findPos();

        if (pos != null) {
            mob.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.0D);
        }
    }

    private Vec3 findPos() {
        Vec3 look = mob.getViewVector(0.0F);

        Vec3 hover = HoverRandomPos.getPos(
                mob,
                8,
                7,
                look.x,
                look.z,
                (float)Math.PI / 2F,
                3,
                1
        );

        if (hover != null) {
            return hover;
        }

        return AirAndWaterRandomPos.getPos(
                mob,
                8,
                4,
                -2,
                look.x,
                look.z,
                (float)Math.PI / 2F
        );
    }
}

