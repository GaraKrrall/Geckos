/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.resources;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.variant.GeckoVariants;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class GeckoResourceLocation {
    public static final Map<GeckoVariants, ResourceLocation> GECKO_RESOURCE_LOCATION_MAP =
            Util.make(Maps.newEnumMap(GeckoVariants.class), map -> {
                map.put(GeckoVariants.BLUE,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_blue.png"));
                map.put(GeckoVariants.PINK,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_pink.png"));
                map.put(GeckoVariants.BROWN,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_brown.png"));
                map.put(GeckoVariants.GREEN,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_green.png"));
                map.put(GeckoVariants.RED,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_red.png"));
                map.put(GeckoVariants.PHANTOM,
                        ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "textures/entity/gecko/gecko_brown.png"));
            });
}
