package mc.garakrral.geckos.entity;

import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.animal.FlyEntity;
import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Geckos.MODID);

    public static final Supplier<EntityType<GeckoEntity>> GECKO =
            ENTITY_TYPES.register("gecko", () -> EntityType.Builder.of(GeckoEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 0.35f).build("gecko"));

    public static final Supplier<EntityType<FlyEntity>> FLY =
            ENTITY_TYPES.register("fly", () -> EntityType.Builder.of(FlyEntity::new, MobCategory.AMBIENT)
                    .sized(0.30f, 0.30f).build("fly"));

}
