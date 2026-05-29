package mc.garakrral.geckos.entity;

import java.util.function.Supplier;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.custom.FlyEntity;
import mc.garakrral.geckos.entity.custom.GeckoEntity;

import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Main.MODID);

    public static final Supplier<EntityType<GeckoEntity>> GECKO =
            ENTITY_TYPES.register("gecko", () -> EntityType.Builder.of(GeckoEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 0.35f).build("gecko"));

    public static final Supplier<EntityType<FlyEntity>> FLY =
            ENTITY_TYPES.register("fly", () -> EntityType.Builder.of(FlyEntity::new, MobCategory.AMBIENT)
                    .sized(0.30f, 0.30f).build("fly"));

}
