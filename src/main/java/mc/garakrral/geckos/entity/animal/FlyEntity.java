/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.animal;

import mc.garakrral.geckos.entity.goal.FlyWanderGoal;
import mc.garakrral.geckos.item.ModItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * Simple flying ambient mob representing a fly.
 *
 * <p>The entity uses airborne navigation, a looping wing animation state, and drops a dead fly item
 * when killed.
 */
public class FlyEntity extends PathfinderMob implements FlyingAnimal {
    /**
     * Creates a fly entity and configures flying movement components.
     *
     * @param t entity type instance
     * @param l current level
     */
    public FlyEntity(EntityType<? extends FlyEntity> t, Level l) {
        super(t, l);
        this.moveControl = new FlyingMoveControl(this, 10, true);
        this.navigation = new FlyingPathNavigation(this, l);
        this.setNoGravity(true);
    }

    public final AnimationState flyAnimationState = new AnimationState();

    /**
     * Registers the fly's AI goals.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FlyWanderGoal(this));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F, 0.02F));
    }

    /**
     * Builds the attribute set used by fly entities.
     *
     * @return mutable attribute builder for the fly type
     */
    public static AttributeSupplier.Builder createAttribute() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.6F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 0)
                .add(Attributes.FOLLOW_RANGE, 48.0);
    }

    /**
     * Creates a flying navigation implementation for the entity.
     *
     * @param level current level
     * @return flying path navigation instance
     */
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new FlyingPathNavigation(this, level);
    }

    /**
     * Updates base behavior and ensures the fly animation is started.
     */
    @Override
    public void tick() {
        super.tick();

        if (this.isFlying()) flyAnimationState.startIfStopped(this.tickCount);
    }

    /**
     * Drops the dead fly item in addition to the default loot behavior.
     *
     * @param level server level handling the death
     * @param damageSource damage source that killed the entity
     * @param recentlyHit whether the entity was recently hit by a player
     */
    @Override
    public void dropCustomDeathLoot(@NotNull ServerLevel level, @NotNull DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        this.spawnAtLocation(ModItems.DEAD_FLY);
    }

    /**
     * Reports that the mob should always be treated as flying.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isFlying() {
        return true;
    }


    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull DamageSource damageSource) {
        return false;
    }
}
