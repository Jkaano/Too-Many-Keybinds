package io.github.jkaano.toomanykeybinds.util;

import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class KeyboardSimulator {

    private long window;
    private final KeyboardHandler handler;

    // Tick counter
    public static int tickCounter = 0;
    public static boolean isCounting = false;
    public static final int DELAY_TIME = 20;

    private static KeyboardSimulator currentInstance = null;

    private int keyCode;

    public KeyboardSimulator() {
        window = Minecraft.getInstance().getWindow().getWindow();
        handler = Minecraft.getInstance().keyboardHandler;
    }

    public void pressKey(int keyCode) {
        this.keyCode = keyCode;
        int scancode = 0;
        int action = GLFW.GLFW_PRESS;
        int modifiers = 0;

        Minecraft.getInstance().execute(() -> {
            if (currentInstance != null) { return; }
            this.handler.keyPress(window, keyCode, scancode, action, modifiers);
        });

        currentInstance = this;

        // Start counter
        isCounting = true;
        tickCounter = 0;
    }

    public void stopPress() {
        Minecraft.getInstance().execute(() -> {
            if (currentInstance == null) { return; }
            this.handler.keyPress(window, this.keyCode, 0, GLFW.GLFW_RELEASE, 0);
        });

        currentInstance = null;
        isCounting = false;
        tickCounter = 0;
    }

    public static KeyboardSimulator getPressedInstance() { return currentInstance;}

}
