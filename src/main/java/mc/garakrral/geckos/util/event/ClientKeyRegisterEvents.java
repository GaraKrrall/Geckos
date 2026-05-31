/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.event;

import mc.garakrral.geckos.client.keybind.KeyBindings;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@OnlyIn(Dist.CLIENT)
public class ClientKeyRegisterEvents {
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.DISMOUNT_KEY);
        event.register(KeyBindings.HEAD);
        event.register(KeyBindings.LEFT);
        event.register(KeyBindings.RIGHT);
        event.register(KeyBindings.HUD_EDITOR);
        event.register(KeyBindings.CARRY_GECKO);
    }
}
