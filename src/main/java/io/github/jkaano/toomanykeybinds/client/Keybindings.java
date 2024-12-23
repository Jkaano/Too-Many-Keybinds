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
            KeyConflictContext.UNIVERSAL,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            CATEGORY_TMK
    );

}
