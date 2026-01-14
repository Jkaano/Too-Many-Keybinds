package io.github.jkaano.toomanykeybinds.gui.screen;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.util.KeyBinding;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

public class KeySelectScreenForge extends KeySelectScreen {

    public KeySelectScreenForge() {
        super();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (key == KeyBinding.TOO_MANY_KEYBINDS_TOGGLE.getKey() && !searchbar.isFocused()) {
            this.onClose();
            return true;
        } else if (isMovementKey(key)) {
            KeyMapping.set(key, true);
            return false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (isMovementKey(key)) {
            KeyMapping.set(key, false);
            return false;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    // Checks if key code is a movement key
    private boolean isMovementKey(InputConstants.Key key) {
        Options options = Minecraft.getInstance().options;
        return  key == options.keyUp.getKey() ||
                key == options.keyDown.getKey() ||
                key == options.keyLeft.getKey() ||
                key == options.keyRight.getKey() ||
                key == options.keyJump.getKey() ||
                key == options.keyShift.getKey();
    }

}
