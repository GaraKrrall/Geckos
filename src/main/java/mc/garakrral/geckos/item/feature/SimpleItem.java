/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.item.feature;

import net.minecraft.world.item.Item;

/**
 * Minimal item implementation used for content that does not require custom behavior.
 *
 * <p>Defining a dedicated subclass still leaves room for future extension while keeping current
 * registrations explicit and semantically named in the item registry.
 */
public class SimpleItem extends Item {
    /**
     * Creates a simple item with the supplied vanilla item properties.
     *
     * @param properties immutable item property configuration used by the base {@link Item} class
     */
    public SimpleItem(Properties properties) {
        super(properties);
    }
}
