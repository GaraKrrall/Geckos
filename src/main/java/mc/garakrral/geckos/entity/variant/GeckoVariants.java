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

import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.Comparator;

public enum GeckoVariants {
    BLUE(0),
    GREEN(1),
    PINK(2),
    BROWN(3),
    RED(4),
    PHANTOM(5),
    @Deprecated(since = "0.1.1") NETHER_BASALT(6);

    private static final GeckoVariants[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(GeckoVariants::getId)).toArray(GeckoVariants[]::new);

    public static final GeckoVariants[] SPAWNABLE_VARIANTS =
            Arrays.stream(GeckoVariants.values())
                    .filter(v -> v != GeckoVariants.NETHER_BASALT)
                    .toArray(GeckoVariants[]::new);

    public static GeckoVariants selectRandomGeckoVariant(RandomSource randomSource) {
        return Util.getRandom(GeckoVariants.SPAWNABLE_VARIANTS, randomSource);
    }

    public static void changeRemovedVariants(GeckoEntity gecko, RandomSource randomSource) {
        GeckoVariants geckoVariant = gecko.getGeckoVariant();
        if (geckoVariant ==  NETHER_BASALT) {
            gecko.setGeckoVariant(selectRandomGeckoVariant(randomSource));
        }
    }

    private final int id;

    GeckoVariants(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static GeckoVariants byId(int id){
        return BY_ID[id % BY_ID.length];
    }
}
