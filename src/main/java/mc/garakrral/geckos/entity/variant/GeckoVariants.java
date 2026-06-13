/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.variant;

import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.resources.GeckoResourceLocation;

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Enumerates all supported visual variants for gecko entities.
 *
 * <p>Each variant carries both a stable numeric id used for save/network state and a logical
 * texture resource identifier used by client rendering code.
 */
public enum GeckoVariants {
    BLUE(0, GeckoResourceLocation.Resources.GECKO_VARIANT_BLUE),
    GREEN(1, GeckoResourceLocation.Resources.GECKO_VARIANT_GREEN),
    PINK(2, GeckoResourceLocation.Resources.GECKO_VARIANT_PINK),
    BROWN(3, GeckoResourceLocation.Resources.GECKO_VARIANT_BROWN),
    RED(4, GeckoResourceLocation.Resources.GECKO_VARIANT_RED),
    PHANTOM(5, GeckoResourceLocation.Resources.GECKO_VARIANT_PHANTOM),
    @Deprecated(since = "0.1.1") NETHER_BASALT(6, GeckoResourceLocation.Resources.NULL);

    private static final GeckoVariants[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(GeckoVariants::getId)).toArray(GeckoVariants[]::new);

    public static final GeckoVariants[] SPAWNABLE_VARIANTS =
            Arrays.stream(GeckoVariants.values())
                    .filter(v -> v != GeckoVariants.NETHER_BASALT)
                    .toArray(GeckoVariants[]::new);

    private final int id;
    private final GeckoResourceLocation.Resources resources;

    /**
     * Creates a variant entry.
     *
     * @param id stable numeric id stored in entity data
     * @param resources logical texture resource mapping for the variant
     */
    GeckoVariants(int id, GeckoResourceLocation.Resources resources) {
        this.id = id;
        this.resources = resources;
    }

    /**
     * Chooses a random spawnable variant.
     *
     * @param randomSource random source used for selection
     * @return randomly selected non-removed gecko variant
     */
    public static GeckoVariants selectRandomGeckoVariant(RandomSource randomSource) {
        return Util.getRandom(GeckoVariants.SPAWNABLE_VARIANTS, randomSource);
    }

    /**
     * Replaces removed legacy variants with a valid modern variant.
     *
     * @param gecko gecko whose variant should be checked
     * @param randomSource random source used if remapping is required
     */
    public static void changeRemovedVariants(GeckoEntity gecko, RandomSource randomSource) {
        if (gecko.getGeckoVariant() == NETHER_BASALT) {
            gecko.setGeckoVariant(selectRandomGeckoVariant(randomSource));
        }
    }

    /**
     * Returns the logical resource enum used to resolve this variant's texture.
     *
     * @return resource enum for the variant texture
     */
    public GeckoResourceLocation.Resources getGeckoResourceLocation() {
        return this.resources;
    }

    /**
     * Returns the stable numeric id of the variant.
     *
     * @return variant id
     */
    public int getId() {
        return id;
    }

    /**
     * Resolves a variant from its numeric id.
     *
     * @param id serialized variant id
     * @return variant mapped from the id modulo the variant table length
     */
    public static GeckoVariants byId(int id){
        return BY_ID[id % BY_ID.length];
    }
}
