package mc.garakrral.geckos.entity.custom;

import mc.garakrral.geckos.entity.client.GeckoClientUtil;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.entity.variant.GeckoVariant;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.entity.goal.GeckoFollowOwnerGoal;
import mc.garakrral.geckos.entity.goal.GeckoLandOnShoulderGoal;
import mc.garakrral.geckos.entity.variant.type.GeckoType;
import mc.garakrral.geckos.entity.custom.base.BaseGeckoEntity;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeckoEntity extends BaseGeckoEntity {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();
    public final AnimationState sleepAnimationState = new AnimationState();

    private static final GeckoType GECKO_TYPE = GeckoType.NORMAL;

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
        super(entityType, level, GECKO_TYPE);
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
        this.goalSelector.addGoal(12, new GeckoFollowOwnerGoal(this, 1.2D, 3.0F, 1.0F, false));

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
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        GeckoEntity babyGecko = ModEntities.GECKO.get().create(level);

        if (babyGecko != null) {
            GeckoVariant v1 = this.getGeckoVariant();
            GeckoVariant v2 = ((GeckoEntity) partner).getGeckoVariant();

            GeckoVariant selected = this.random.nextBoolean() ? v1 : v2;
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
            }
            else {
                idleAnimationState.startIfStopped(tickCount);
            }
        }
        else {
            idleDelay = 3;
            idleAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (isServerSide()) {

            if (this.isCarried()) {
                this.carriedEvent();
            } else {
                this.setNoAi(false);
            }

            if (this.isSittingGecko() || this.isMorningSleeping()) {
                this.setSleepingGecko(true);
            }

            else if (level().isNight() && !isInWaterOrBubble() && !isSittingGecko() && !hasTarget()) {
                sleepCheckCooldown--;

                if (sleepCheckCooldown <= 0) {
                    sleepCheckCooldown = SLEEP_STATE_CHECK_INTERVAL;

                    if (random.nextInt(SLEEP_CHANCE) == 0) {
                        setSleepingGecko(true);

                        navigation.stop();
                        setDeltaMovement(0, getDeltaMovement().y, 0);
                    }
                }
            }

            else if (level().isDay() && !isInWaterOrBubble()) {
               wakeUpCheckCooldown --;

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
    public boolean hurt(DamageSource s, float a) {
        if (this.isPassenger()) {
            return false;
        }
        return super.hurt(s, a);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
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
    public InteractionResult mobInteract(Player p, @NotNull InteractionHand hand) {
        ItemStack item = p.getItemInHand(hand);

        if (item.is(ModItems.DEAD_FLY.get())) {
            if (this.level().isNight()) return InteractionResult.FAIL;

            if (!this.isTame() && !this.isSleepingGecko() && !this.isSittingGecko()) {
                if (this.isServerSide()) {
                    if (this.random.nextInt(TAME_CHANCE) == 0) {
                        this.tame(p);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }

                    if (!p.getAbilities().instabuild) item.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            else if (this.isInjured() && this.isTame() && !this.isSleepingGecko() && !this.isSittingGecko()) {
                if (this.isServerSide()) {
                    this.heal(1.0F);

                    this.createHeartParticles(3, 0);
                    if (!p.getAbilities().instabuild) item.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            else if (this.isTame() && this.getAge() == 0 && !this.isInLove() && !this.isSleepingGecko() && !this.isSittingGecko() && this.isMaxHealth()) {
                if (this.isBaby()) return InteractionResult.FAIL;

                if (this.isServerSide()) {
                    this.setInLove(p);

                    if (!p.getAbilities().instabuild) item.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }

        if (this.isTame() && p.getMainHandItem().isEmpty()) {
            if (this.isSleepingGecko() && this.level().isNight()) return InteractionResult.FAIL;

            if (isServerSide()) {
                if (!p.equals(this.getOwner())) return InteractionResult.FAIL;

                this.setSleepingGecko(false);
                this.setSittingGecko(!this.isSittingGecko());
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(p, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag c) {
        super.addAdditionalSaveData(c);
        c.putInt("Variant", this.getTypeGeckoVariant());
        c.putBoolean("Sleeping", this.isSleepingGecko());
        c.putBoolean("Sitting", this.isSittingGecko());
        c.putBoolean("OnShoulder", this.isShoulderRiding());
        c.putBoolean("MorningSleeping", this.isMorningSleeping());
        c.putBoolean("OnHead", this.isOnHead());
        c.putBoolean("Carried", this.isCarried());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag c) {
        super.readAdditionalSaveData(c);
        this.entityData.set(VARIANT, c.getInt("Variant") & 255);

        if (c.contains("Sleeping")) {
            this.entityData.set(SLEEPING, c.getBoolean("Sleeping"));
        } else {
            this.entityData.set(SLEEPING, false);
        }
        if (c.contains("Sitting")) {
            this.setSittingGecko(c.getBoolean("Sitting"));
        }
        if (c.contains("OnShoulder")) {
            this.setShoulderRiding(c.getBoolean("OnShoulder"));
        }
        if (c.contains("MorningSleeping")) {
            this.setMorningSleeping(c.getBoolean("MorningSleeping"));
        }
        if (c.contains("OnHead")) {
            this.setOnHead(c.getBoolean("OnHead"));
        }
        if (c.contains("Carried")) {
            this.setCarried(c.getBoolean("Carried"));
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

    public boolean isCarried () {
        return this.entityData.get(CARRIED);
    }

    public void setCarried(boolean carried) {
        this.entityData.set(CARRIED, carried);

        if (!carried) {
            this.setNoAi(false);
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    public int getShoulderCooldown() {
        return this.shoulderCooldown;
    }
    public void setShoulderCooldown(int cooldown) {

        this.shoulderCooldown = Math.max(0, Math.min(cooldown, MAX_SHOULDER_COOLDOWN));
    }

    public float getCarryDistance() {
        return carryDistance;
    }

    public void setCarryDistance(float dist) {
        carryDistance = Math.max(0.8F, Math.min(dist, 3F));
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

    public GeckoVariant getGeckoVariant() {
        return GeckoVariant.byId(this.getTypeGeckoVariant() & 255);
    }

    protected void setGeckoVariant(GeckoVariant v) {
        this.entityData.set(VARIANT, v.getId() & 255);
    }

    public boolean isMaxHealth() {
        return this.getHealth() >= this.getMaxHealth();
    }

    public boolean isInjured() {
        return this.getHealth() < this.getMaxHealth();
    }


    public void createHeartParticles(int count, int speed) {
        ((ServerLevel) level()).sendParticles(ParticleTypes.HEART, getX(), getY() + 1, getZ(),
                count, 0.3, 0.3, 0.3, speed);
    }

    private void carriedEvent() {
        var owner = this.getOwner();

        if (!(owner instanceof Player player) || !player.isAlive()) {
            this.setCarried(false);
            return;
        }

        this.setShoulderRiding(false);

        navigation.stop();
        setTarget(null);
        setDeltaMovement(Vec3.ZERO);
        fallDistance = 0;
        setNoAi(true);
        Vec3 eyePos = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();

        double minDist = player.getBbWidth() + getBbWidth() + 0.35D;
        double wantedDist = Mth.clamp(getCarryDistance(), minDist, MAX_CARRY_DISTANCE);

        Vec3 target = eyePos.add(look.scale(wantedDist));
        HitResult hit = level().clip(new ClipContext(eyePos, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (hit.getType() != HitResult.Type.MISS) {
            target =
                    hit.getLocation()
                            .subtract(look.scale(getBbWidth() + 0.25D));
        }

        double y = target.y - getBbHeight() / 2D;
        Vec3 finalPos = new Vec3(target.x, y, target.z);
        AABB box = getBoundingBox().move(finalPos.subtract(position()));

        if (!level().noCollision(this, box)) {
            return;
        }

        moveTo(finalPos.x, finalPos.y, finalPos.z, player.getYRot(), player.getXRot());
    }

    @NotNull
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType,
                                                 @Nullable SpawnGroupData spawnGroupData) {

        GeckoVariant variant = Util.getRandom(GeckoVariant.values(), this.random);
        this.setGeckoVariant(variant);

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}
