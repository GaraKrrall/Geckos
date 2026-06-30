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

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;

/**
 * Client-only helper responsible for keybinding registration.
 *
 * <p>The key mappings themselves are declared elsewhere, but this class provides the lifecycle hook
 * that exposes them to Minecraft's input system during client initialization.
 */
@OnlyIn(Dist.CLIENT)
public class ClientKeyRegisterEvents {
    /**
     * Registers every key mapping used by the mod.
     *
     * @param event key mapping registration event emitted on the client
     */
    public static void registerKeys(RegisterKeyMappingsEvent event) {
      //  event.register(KeyBindings.DISMOUNT_KEY);
        //event.register(KeyBindings.HEAD);
        event.register(KeyBindings.LEFT);
        event.register(KeyBindings.RIGHT);
        event.register(KeyBindings.HUD_EDITOR);
        event.register(KeyBindings.CARRY_GECKO);
    }
}
