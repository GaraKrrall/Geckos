/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.animal;

import mc.garakrral.geckos.client.GeckoClientUtil;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.entity.goal.GeckoFollowOwnerGoal;
import mc.garakrral.geckos.entity.goal.GeckoLandOnShoulderGoal;
import mc.garakrral.geckos.entity.variant.GeckoVariants;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.util.event.ServerGeckoCarryEvents;
import mc.garakrral.geckos.util.event.ServerParticlesEvent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Core gecko mob implementation covering taming, carrying, sleeping, breeding, and shoulder logic.
 *
 * <p>This entity combines normal tameable-animal behavior with several custom interaction states
 * such as being carried, mounted on a player's head, and autonomous sleep handling.
 */
public class GeckoEntity extends ShoulderRidingEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    public final AnimationState sleepAnimationState = new AnimationState();

    protected static final int SLEEP_STATE_CHECK_INTERVAL = 100;
    protected static final int SLEEP_CHANCE = 3;
    protected static final int WAKEUP_CHANCE = 3;
    protected static final int TAME_CHANCE = 3;
    protected static final int MAX_SHOULDER_COOLDOWN = 100;
    protected static final int HEAL_INTERVAL = 40;
    protected static final float CARRY_DISTANCE = 1.0F;

    protected int idleDelay = 3;
    protected int shoulderCooldown = 0;
    protected float carryDistance = CARRY_DISTANCE;
    protected int sleepCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;
    protected int wakeUpCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;

    protected static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.INT);

    protected static final EntityDataAccessor<Boolean> SLEEPING =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> MORNING_SLEEPING =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> SHOULDER_RIDING =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> ON_HEAD =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> CARRIED =
            SynchedEntityData.defineId(GeckoEntity.class, EntityDataSerializers.BOOLEAN);

    public static final float MIN_CARRY_DISTANCE = 1.2F;
    public static final float MAX_CARRY_DISTANCE = 3.0F;

    /**
     * Creates a gecko entity instance.
     *
     * @param entityType entity type definition
     * @param level current level
     */
    public GeckoEntity(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Registers all AI and target goals used by the gecko.
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.6F));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new GeckoLandOnShoulderGoal(this));
        this.goalSelector.addGoal(5, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(6, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(7, new TemptGoal(this, 1.25, stack -> stack.is(ModItems.DEAD_FLY), false));
        this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.25));

        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(12, new GeckoFollowOwnerGoal(this, 1.2D, 3.0F, 1.0F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, FlyEntity.class, true));
    }

    /**
     * Builds the attribute set used by gecko entities.
     *
     * @return mutable attribute builder for geckos
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.25d)
                .add(Attributes.FOLLOW_RANGE, 24d)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    /**
     * Checks whether the supplied item is valid gecko food.
     *
     * @param food tested item stack
     * @return {@code true} when the stack is a dead fly
     */
    @Override
    public boolean isFood(ItemStack food) {
        return food.is(ModItems.DEAD_FLY);
    }

    /**
     * Creates offspring during breeding and inherits one parent's variant.
     *
     * @param level server level creating the child
     * @param partner breeding partner
     * @return spawned baby gecko or {@code null} if creation failed
     */
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        GeckoEntity babyGecko = ModEntities.GECKO.get().create(level);

        if (babyGecko != null) {
            GeckoVariants v1 = this.getGeckoVariant();
            GeckoVariants v2 = ((GeckoEntity) partner).getGeckoVariant();

            GeckoVariants selected = this.random.nextBoolean() ? v1 : v2;
            babyGecko.setGeckoVariant(selected);

            if (this.isTame()) {
                babyGecko.setOwnerUUID(getOwnerUUID());
                babyGecko.setTame(true, true);
            }
        }
        return babyGecko;
    }

    /**
     * Starts and stops animation states based on the gecko's current movement and sleep state.
     */
    protected void setupAnimationStates() {
        boolean sleeping = this.isSleepingGecko();
        boolean inWater = this.isInWaterOrBubble();
        boolean isMoving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;

        if (sleeping && !inWater) {
            if (!this.sleepAnimationState.isStarted()) {
                this.sleepAnimationState.start(this.tickCount);
            }
            this.idleAnimationState.stop();
            this.swimAnimationState.stop();
            return;
        }

        if (!sleeping && this.sleepAnimationState.isStarted()) {
            this.sleepAnimationState.stop();
        }

        if (inWater) {
            this.swimAnimationState.startIfStopped(this.tickCount);
            this.idleAnimationState.stop();
            return;
        } else {
            this.swimAnimationState.stop();
        }

        if (!isMoving && !isInWaterOrBubble()) {
            if (idleDelay > 0) {
                idleDelay--;
            } else {
                idleAnimationState.startIfStopped(tickCount);
            }
        } else {
            idleDelay = 3;
            idleAnimationState.stop();
        }
    }

    /**
     * Advances gecko behavior each tick, including carry logic, sleep checks, and client preview
     * updates.
     */
    @Override
    public void tick() {
        super.tick();

        if (isServerSide()) {

            if (this.isCarried()) {
                ServerGeckoCarryEvents.updateCarriedServerPosition(this);
            } else {
                this.setNoAi(false);
            }

            if (this.isSittingGecko() || this.isMorningSleeping()) {
                this.setSleepingGecko(true);
            } else if (level().isNight() && !isInWaterOrBubble() && !isSittingGecko() && !hasTarget()) {
                sleepCheckCooldown--;

                if (sleepCheckCooldown <= 0) {
                    sleepCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;

                    if (random.nextInt(SLEEP_CHANCE) == 0) {
                        setSleepingGecko(true);

                        navigation.stop();
                        setDeltaMovement(0, getDeltaMovement().y, 0);
                    }
                }
            } else if (level().isDay() && !isInWaterOrBubble()) {
                wakeUpCheckCooldown--;

                if (wakeUpCheckCooldown <= 0) {
                    wakeUpCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;

                    if (random.nextInt(WAKEUP_CHANCE) == 0) {
                        setSleepingGecko(false);
                    }
                }
            } else {
                sleepCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;
                wakeUpCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;
            }

            if (this.isSittingGecko() && this.isSleepingGecko()) {
                if (this.tickCount % HEAL_INTERVAL == 0) {
                    this.heal(1.0F);
                }
            }
        }

        if (isClientSide()) {
            if (this.isCarried()) {
                GeckoClientUtil.clientCarryPreview(this);
            }

            this.setupAnimationStates();
        }

        if (shoulderCooldown > 0) shoulderCooldown--;
    }

    /**
     * Treats sleeping or sitting geckos as immobile.
     *
     * @return {@code true} when the gecko should not move
     */
    @Override
    protected boolean isImmobile() {
        return this.isSleepingGecko() || this.isSittingGecko() || super.isImmobile();
    }

    /**
     * Prevents passenger geckos from taking damage.
     *
     * @param source damage source
     * @param amount incoming damage amount
     * @return {@code true} if damage was applied
     */
    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isPassenger()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    /**
     * Defines the synchronized entity data tracked for gecko state.
     *
     * @param builder synched data builder
     */
    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(SLEEPING, false);
        builder.define(SITTING, false);
        builder.define(SHOULDER_RIDING, false);
        builder.define(MORNING_SLEEPING, false);
        builder.define(ON_HEAD, false);
        builder.define(CARRIED, false);
    }

    /**
     * Handles direct player interaction with the gecko.
     *
     * @param player interacting player
     * @param hand hand used for interaction
     * @return interaction result indicating whether the action was consumed
     */
    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        InteractionResult foodResult = handleDeadFlyInteraction(player, item);
        if (foodResult.consumesAction()) {
            return foodResult;
        }

        InteractionResult sitResult = handleSitInteraction(player);
        if (sitResult.consumesAction()) {
            return sitResult;
        }

        return super.mobInteract(player, hand);
    }

    /**
     * Writes custom gecko state to NBT for persistence.
     *
     * @param tag destination compound tag
     */
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getTypeGeckoVariant());
        tag.putBoolean("Sleeping", this.isSleepingGecko());
        tag.putBoolean("Sitting", this.isSittingGecko());
        tag.putBoolean("OnShoulder", this.isShoulderRiding());
        tag.putBoolean("MorningSleeping", this.isMorningSleeping());
        tag.putBoolean("OnHead", this.isOnHead());
        tag.putBoolean("Carried", this.isCarried());
    }

    /**
     * Reads custom gecko state back from NBT.
     *
     * @param tag source compound tag
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(VARIANT, tag.getInt("Variant") & 255);
        GeckoVariants.changeRemovedVariants(this, this.random);

        if (tag.contains("Sleeping")) {
            this.entityData.set(SLEEPING, tag.getBoolean("Sleeping"));
        } else {
            this.entityData.set(SLEEPING, false);
        }
        if (tag.contains("Sitting")) {
            this.setSittingGecko(tag.getBoolean("Sitting"));
        }
        if (tag.contains("OnShoulder")) {
            this.setShoulderRiding(tag.getBoolean("OnShoulder"));
        }
        if (tag.contains("MorningSleeping")) {
            this.setMorningSleeping(tag.getBoolean("MorningSleeping"));
        }
        if (tag.contains("OnHead")) {
            this.setOnHead(tag.getBoolean("OnHead"));
        }
        if (tag.contains("Carried")) {
            this.setCarried(tag.getBoolean("Carried"));
        }
    }

    /**
     * Routes dead-fly interactions into taming, healing, or breeding behavior.
     *
     * @param player interacting player
     * @param item held item stack
     * @return interaction result for the attempted action
     */
    private InteractionResult handleDeadFlyInteraction(Player player, ItemStack item) {
        if (!item.is(ModItems.DEAD_FLY.get())) {
            return InteractionResult.PASS;
        }

        if (this.level().isNight()) {
            return InteractionResult.FAIL;
        }

        if (canBeTamed()) {
            return tameGecko(player, item);
        }

        if (canBeHealed()) {
            return healGecko(player, item);
        }

        if (canBreed()) {
            return breedGecko(player, item);
        }

        return InteractionResult.PASS;
    }

    /**
     * Attempts to tame the gecko using a dead fly.
     *
     * @param player player attempting the tame
     * @param item consumed item
     * @return success result for the interaction
     */
    private InteractionResult tameGecko(Player player, ItemStack item) {
        if (this.isServerSide()) {
            if (this.random.nextInt(TAME_CHANCE) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }

            consumeItem(player, item);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Heals the gecko and spawns heart particles.
     *
     * @param player interacting player
     * @param item consumed item
     * @return success result for the interaction
     */
    private InteractionResult healGecko(Player player, ItemStack item) {
        if (this.isServerSide()) {
            this.heal(1.0F);

            ServerParticlesEvent.createHeartParticles(this, 3, 0);

            consumeItem(player, item);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Places the gecko into love mode for breeding.
     *
     * @param player interacting player
     * @param item consumed item
     * @return interaction result
     */
    private InteractionResult breedGecko(Player player, ItemStack item) {
        if (this.isBaby()) {
            return InteractionResult.FAIL;
        }

        if (this.isServerSide()) {
            this.setInLove(player);

            consumeItem(player, item);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Toggles the gecko's sitting state when its owner interacts with an empty hand.
     *
     * @param player interacting player
     * @return interaction result describing whether the sit toggle ran
     */
    private InteractionResult handleSitInteraction(Player player) {
        if (!this.isTame() || !player.getMainHandItem().isEmpty()) {
            return InteractionResult.PASS;
        }

        if (this.isSleepingGecko() && this.level().isNight()) {
            return InteractionResult.FAIL;
        }

        if (this.isServerSide()) {
            if (!player.equals(this.getOwner())) {
                return InteractionResult.FAIL;
            }

            this.setSleepingGecko(false);
            this.setSittingGecko(!this.isSittingGecko());
            this.navigation.stop();
            this.setTarget(null);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * Checks whether the gecko is currently eligible to be tamed.
     *
     * @return {@code true} if taming can proceed
     */
    private boolean canBeTamed() {
        return !this.isTame() && !this.isSleepingGecko() && !this.isSittingGecko();
    }

    /**
     * Checks whether the gecko is eligible to be healed by the held item.
     *
     * @return {@code true} if healing can proceed
     */
    private boolean canBeHealed() {
        return this.isInjured() && this.isTame() && !this.isSleepingGecko() && !this.isSittingGecko();
    }

    /**
     * Checks whether the gecko can currently enter breeding state.
     *
     * @return {@code true} if breeding conditions are satisfied
     */
    public boolean canBreed() {
        return this.isTame() && this.getAge() == 0 && !this.isInLove() && !this.isSleepingGecko() && !this.isSittingGecko() && this.isMaxHealth();
    }

    /**
     * Consumes one item from the stack unless the player is in creative mode.
     *
     * @param player consuming player
     * @param stack item stack to shrink
     */
    private static void consumeItem(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    /**
     * Returns whether the gecko is currently sleeping.
     *
     * @return sleeping flag
     */
    public boolean isSleepingGecko() {
        return this.entityData.get(SLEEPING);
    }

    /**
     * Updates the sleeping flag.
     *
     * @param s new sleeping state
     */
    public void setSleepingGecko(boolean s) {
        this.entityData.set(SLEEPING, s);
    }

    /**
     * Returns whether the gecko is stored in a shoulder-riding state.
     *
     * @return shoulder-riding flag
     */
    public boolean isShoulderRiding() {
        return this.entityData.get(SHOULDER_RIDING);
    }

    /**
     * Updates the shoulder-riding flag.
     *
     * @param os new shoulder-riding state
     */
    public void setShoulderRiding(boolean os) {
        this.entityData.set(SHOULDER_RIDING, os);
    }

    /**
     * Returns whether the gecko is sitting.
     *
     * @return sitting flag
     */
    public boolean isSittingGecko() {
        return this.entityData.get(SITTING);
    }

    /**
     * Updates the sitting flag.
     *
     * @param sit new sitting state
     */
    public void setSittingGecko(boolean sit) {
        this.entityData.set(SITTING, sit);
    }

    /**
     * Returns whether the gecko is forced into the morning-sleeping state.
     *
     * @return morning sleeping flag
     */
    public boolean isMorningSleeping() {
        return this.entityData.get(MORNING_SLEEPING);
    }

    /**
     * Updates the morning-sleeping flag.
     *
     * @param morningSleeping new flag value
     */
    public void setMorningSleeping(boolean morningSleeping) {
        this.entityData.set(MORNING_SLEEPING, morningSleeping);
    }

    /**
     * Returns whether the gecko is stored as head-mounted data.
     *
     * @return head-mounted flag
     */
    public boolean isOnHead() {
        return this.entityData.get(ON_HEAD);
    }

    /**
     * Updates the head-mounted flag.
     *
     * @param onHead new head-mounted state
     */
    public void setOnHead(boolean onHead) {
        this.entityData.set(ON_HEAD, onHead);
    }

    /**
     * Returns whether the gecko is currently being carried.
     *
     * @return carry flag
     */
    public boolean isCarried() {
        return this.entityData.get(CARRIED);
    }

    /**
     * Updates the carried flag and restores normal movement state when carry mode ends.
     *
     * @param carried new carry state
     */
    public void setCarried(boolean carried) {
        this.entityData.set(CARRIED, carried);

        if (!carried) {
            this.setNoAi(false);
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    /**
     * Sets the cooldown that delays immediate remounting onto a shoulder.
     *
     * @param cooldown requested cooldown value
     */
    public void setShoulderCooldown(int cooldown) {
        this.shoulderCooldown = Math.clamp(cooldown, 0, MAX_SHOULDER_COOLDOWN);
    }

    /**
     * Returns the configured carry distance.
     *
     * @return carry distance in blocks
     */
    public float getCarryDistance() {
        return carryDistance;
    }

    /**
     * Updates the carry distance while clamping it to the supported range.
     *
     * @param dist requested carry distance
     */
    public void setCarryDistance(float dist) {
        carryDistance = Math.clamp(dist, 0.8F, 3F);
    }

    /**
     * Convenience helper indicating whether logic is running on the server.
     *
     * @return {@code true} on the logical server
     */
    protected boolean isServerSide() {
        return !this.level().isClientSide;
    }

    /**
     * Convenience helper indicating whether logic is running on the client.
     *
     * @return {@code true} on the logical client
     */
    protected boolean isClientSide() {
        return this.level().isClientSide;
    }

    /**
     * Returns whether the gecko currently has an attack target.
     *
     * @return {@code true} if the target reference is non-null
     */
    public boolean hasTarget() {
        return this.getTarget() != null;
    }

    /**
     * Returns the raw integer variant id stored in synced data.
     *
     * @return serialized variant id
     */
    protected int getTypeGeckoVariant() {
        return this.entityData.get(VARIANT);
    }

    /**
     * Resolves the current gecko variant enum.
     *
     * @return decoded gecko variant
     */
    public GeckoVariants getGeckoVariant() {
        return GeckoVariants.byId(this.getTypeGeckoVariant() & 255);
    }

    /**
     * Stores a new gecko variant in synchronized entity data.
     *
     * @param v new gecko variant
     */
    public void setGeckoVariant(GeckoVariants v) {
        this.entityData.set(VARIANT, v.getId() & 255);
    }

    /**
     * Checks whether the gecko is at full health.
     *
     * @return {@code true} when current health is at least max health
     */
    public boolean isMaxHealth() {
        return this.getHealth() >= this.getMaxHealth();
    }

    /**
     * Checks whether the gecko is below full health.
     *
     * @return {@code true} when the gecko can still be healed
     */
    public boolean isInjured() {
        return this.getHealth() < this.getMaxHealth();
    }

    /**
     * Finalizes initial spawn data and assigns a random gecko variant.
     *
     * @param level server-level accessor used for spawning
     * @param difficulty local difficulty data
     * @param spawnType reason for the spawn
     * @param spawnGroupData prior spawn group data
     * @return final spawn group data from the superclass
     */
    @NotNull
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {

        GeckoVariants geckoVariant = GeckoVariants.selectRandomGeckoVariant(this.random);
        this.setGeckoVariant(geckoVariant);

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}
