/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.resources;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.entity.variant.GeckoVariants;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GeckoResourceLocation {
    public enum Resources {
        GECKO_PAPER,
        GECKO_PAPER_OLD,
        GECKO_VARIANT_PINK,
        GECKO_VARIANT_GREEN,
        GECKO_VARIANT_BROWN,
        GECKO_VARIANT_RED,
        GECKO_VARIANT_BLUE,
        GECKO_VARIANT_PHANTOM,
        FLY,
        NULL
    }

    @NotNull
    private static final Map<Resources, ResourceLocation> PATH_MAP =
            Util.make(Maps.newEnumMap(Resources.class), map -> {
                map.put(Resources.GECKO_PAPER,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/gui/gecko_hud/gecko_paper.png"));
                map.put(Resources.GECKO_PAPER_OLD,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/gui/gecko_hud/gecko_paper_old.png"));
                map.put(Resources.GECKO_VARIANT_BLUE,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_blue.png"));
                map.put(Resources.GECKO_VARIANT_PINK,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_pink.png"));
                map.put(Resources.GECKO_VARIANT_GREEN,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_green.png"));
                map.put(Resources.GECKO_VARIANT_BROWN,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_brown.png"));
                map.put(Resources.GECKO_VARIANT_RED,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_red.png"));
                map.put(Resources.GECKO_VARIANT_PHANTOM,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_brown.png"));
                map.put(Resources.FLY,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/fly/fly.png"));
            });

   public static ResourceLocation getFromResources(Resources resources) {
      return getPathMap().get(resources);
   }

    public static ResourceLocation getGeckoTextureFromResources(GeckoEntity entity) {
        return getGeckoTextureFromResources(entity.getGeckoVariant());
    }

    public static ResourceLocation getGeckoTextureFromResources(GeckoVariants variant) {
        return getPathMap().get(variant.getGeckoResourceLocation());
    }

   public static Map<Resources, ResourceLocation> getPathMap () {
       return PATH_MAP;
   }
}
