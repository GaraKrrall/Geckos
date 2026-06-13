/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.attachment;

import mc.garakrral.geckos.Geckos;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * Registers custom attachment types used by the mod.
 *
 * <p>Attachments provide a structured way to associate additional data with existing NeoForge game
 * objects without creating custom subclasses. This mod uses an attachment to persist and synchronize
 * the gecko currently mounted on a player's head.
 */
public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Geckos.MODID);

    public static final Supplier<AttachmentType<CompoundTag>> HEAD_GECKO =
            ATTACHMENT_TYPES.register("head_gecko",
                    () -> AttachmentType.builder(() -> new CompoundTag())
                            .serialize(CompoundTag.CODEC)
                            .build());

    /**
     * Registers the attachment deferred register on the supplied mod event bus.
     *
     * @param bus mod event bus responsible for processing deferred registration
     */
    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
