package io.github.jkaano.toomanykeybinds.client;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings(){}

    private static final String CATEGORY_TMK = "key.categories." + TooManyKeybinds.MODID;


    public final KeyMapping openTMK = new KeyMapping(
            "key." + TooManyKeybinds.MODID + ".open_tmk",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            CATEGORY_TMK
    );
    //To avoid conflicts, this button has to be completely unique
    public final KeyMapping keySetter = new KeyMapping(
            "key." + TooManyKeybinds.MODID + ".set_key",
            KeyConflictContext.IN_GAME, //Set to in-game so GUI elements with this keybind should still be fine
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Y,
            CATEGORY_TMK
    );

    public final KeyMapping unbound = new KeyMapping(
            "key." + TooManyKeybinds.MODID + ".unbind",
            KeyConflictContext.GUI,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN,
            CATEGORY_TMK
    );

}
