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

    public GeckoEntity(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }

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

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.25d)
                .add(Attributes.FOLLOW_RANGE, 24d)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean isFood(ItemStack food) {
        return food.is(ModItems.DEAD_FLY);
    }

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

    @Override
    protected boolean isImmobile() {
        return this.isSleepingGecko() || this.isSittingGecko() || super.isImmobile();
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isPassenger()) {
            return false;
        }
        return super.hurt(source, amount);
    }

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

    private InteractionResult healGecko(Player player, ItemStack item) {
        if (this.isServerSide()) {
            this.heal(1.0F);

            ServerParticlesEvent.createHeartParticles(this, 3, 0);

            consumeItem(player, item);
        }

        return InteractionResult.SUCCESS;
    }

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

    private boolean canBeTamed() {
        return !this.isTame() && !this.isSleepingGecko() && !this.isSittingGecko();
    }

    private boolean canBeHealed() {
        return this.isInjured() && this.isTame() && !this.isSleepingGecko() && !this.isSittingGecko();
    }

    public boolean canBreed() {
        return this.isTame() && this.getAge() == 0 && !this.isInLove() && !this.isSleepingGecko() && !this.isSittingGecko() && this.isMaxHealth();
    }

    private static void consumeItem(Player player, ItemStack stack) {
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    public boolean isSleepingGecko() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleepingGecko(boolean s) {
        this.entityData.set(SLEEPING, s);
    }

    public boolean isShoulderRiding() {
        return this.entityData.get(SHOULDER_RIDING);
    }

    public void setShoulderRiding(boolean os) {
        this.entityData.set(SHOULDER_RIDING, os);
    }

    public boolean isSittingGecko() {
        return this.entityData.get(SITTING);
    }

    public void setSittingGecko(boolean sit) {
        this.entityData.set(SITTING, sit);
    }

    public boolean isMorningSleeping() {
        return this.entityData.get(MORNING_SLEEPING);
    }

    public void setMorningSleeping(boolean morningSleeping) {
        this.entityData.set(MORNING_SLEEPING, morningSleeping);
    }

    public boolean isOnHead() {
        return this.entityData.get(ON_HEAD);
    }

    public void setOnHead(boolean onHead) {
        this.entityData.set(ON_HEAD, onHead);
    }

    public boolean isCarried() {
        return this.entityData.get(CARRIED);
    }

    public void setCarried(boolean carried) {
        this.entityData.set(CARRIED, carried);

        if (!carried) {
            this.setNoAi(false);
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    public void setShoulderCooldown(int cooldown) {
        this.shoulderCooldown = Math.clamp(cooldown, 0, MAX_SHOULDER_COOLDOWN);
    }

    public float getCarryDistance() {
        return carryDistance;
    }

    public void setCarryDistance(float dist) {
        carryDistance = Math.clamp(dist, 0.8F, 3F);
    }

    protected boolean isServerSide() {
        return !this.level().isClientSide;
    }

    protected boolean isClientSide() {
        return this.level().isClientSide;
    }

    public boolean hasTarget() {
        return this.getTarget() != null;
    }

    protected int getTypeGeckoVariant() {
        return this.entityData.get(VARIANT);
    }

    public GeckoVariants getGeckoVariant() {
        return GeckoVariants.byId(this.getTypeGeckoVariant() & 255);
    }

    public void setGeckoVariant(GeckoVariants v) {
        this.entityData.set(VARIANT, v.getId() & 255);
    }

    public boolean isMaxHealth() {
        return this.getHealth() >= this.getMaxHealth();
    }

    public boolean isInjured() {
        return this.getHealth() < this.getMaxHealth();
    }

    @NotNull
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType,
                                        @Nullable SpawnGroupData spawnGroupData) {

        GeckoVariants geckoVariant = GeckoVariants.selectRandomGeckoVariant(this.random);
        this.setGeckoVariant(geckoVariant);

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}
