package mc.garakrral.geckos.attachment;

import mc.garakrral.geckos.Main;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Main.MODID);

    public static final Supplier<AttachmentType<CompoundTag>> HEAD_GECKO =
            ATTACHMENT_TYPES.register("head_gecko",
                    () -> AttachmentType.builder(() -> new CompoundTag())
                            .serialize(CompoundTag.CODEC)
                            .build());

    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}