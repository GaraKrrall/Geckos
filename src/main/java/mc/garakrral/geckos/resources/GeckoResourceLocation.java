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

/**
 * Central lookup helper for texture and GUI resource locations used by the mod.
 *
 * <p>Instead of scattering raw path strings throughout rendering and UI code, this class exposes a
 * small enum-backed registry. That keeps path construction consistent and makes it easier to update
 * asset locations without touching every call site individually.
 */
public class GeckoResourceLocation {
    /**
     * Enumerates the known logical resource identifiers used by this helper.
     */
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

    /**
     * Resolves a logical resource enum entry to its concrete {@link ResourceLocation}.
     *
     * @param resources logical resource identifier
     * @return namespaced path mapped to the supplied enum entry
     */
    public static ResourceLocation getFromResources(Resources resources) {
        return getPathMap().get(resources);
    }

    /**
     * Resolves the current texture for a specific gecko entity instance.
     *
     * @param entity gecko entity whose active variant should determine the texture path
     * @return resource location of the entity's current variant texture
     */
    public static ResourceLocation getGeckoTextureFromResources(GeckoEntity entity) {
        return getGeckoTextureFromResources(entity.getGeckoVariant());
    }

    /**
     * Resolves the texture associated with a specific gecko variant.
     *
     * @param variant gecko variant enum value
     * @return resource location mapped to that variant's texture
     */
    public static ResourceLocation getGeckoTextureFromResources(GeckoVariants variant) {
        return getPathMap().get(variant.getGeckoResourceLocation());
    }

    /**
     * Exposes the immutable enum-to-resource mapping used by this helper.
     *
     * @return backing resource lookup map
     */
    public static Map<Resources, ResourceLocation> getPathMap () {
        return PATH_MAP;
    }
}
