package io.github.jkaano.toomanykeybinds.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY = "key.category.toomanykeybinds";
    public static final String KEY_TOO_MANY_KEYBINDS = "key.toomanykeybinds";

    public static final KeyMapping TOO_MANY_KEYBINDS_TOGGLE = new KeyMapping(
            KEY_TOO_MANY_KEYBINDS,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            KEY_CATEGORY
    );

}
