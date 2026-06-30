package mc.garakrral.geckos.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static final String CATEGORY = "key.category.geckos";

    public static final KeyMapping DISMOUNT_KEY = new KeyMapping(
            "key.geckos.dismount",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_SHIFT,
            CATEGORY
    );
    public static final KeyMapping HEAD =
            new KeyMapping(
                    "key.geckos.head",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    CATEGORY
            );

    public static final KeyMapping LEFT =
            new KeyMapping(
                    "key.geckos.left",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_G,
                    CATEGORY
            );

    public static final KeyMapping RIGHT =
            new KeyMapping(
                    "key.geckos.right",
                    KeyConflictContext.IN_GAME,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_H,
                    CATEGORY
            );

    public static final KeyMapping HUD_EDITOR = new KeyMapping(
            "key.geckos.hud_editor",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            CATEGORY
    );

    public static final KeyMapping CARRY_GECKO = new KeyMapping(
            "key.geckos.carry_gecko",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            CATEGORY
    );
}