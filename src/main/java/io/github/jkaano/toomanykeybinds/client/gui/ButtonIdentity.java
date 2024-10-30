package io.github.jkaano.toomanykeybinds.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class gives each button a unique identity based on the which Key identity it was created with.
 * It also handles the buttons actions, by moving it into its own class each button is its own object with handler.
 */
public class ButtonIdentity{

    private final Component name;
    private final KeyMapping key;
    private final KeyModifier modifier;
    private int leftPos, topPos, width, height;

    private final KeyMapping keySetter = Keybindings.INSTANCE.keySetter;
    private final KeyMapping unbound = Keybindings.INSTANCE.unbound;

    private InputConstants.Key tempKey;
    private InputConstants.Key tempSetter;

    private final Timer timer = new Timer();

    public ButtonIdentity(KeyMapping key){
        this.key = key;
        this.name = Component.translatable(key.getName());
        this.modifier = key.getKeyModifier();
    }

    public void setRegion(int leftPos, int topPos, int width, int height){
        this.leftPos = leftPos;
        this.topPos = topPos;
        this.width = width;
        this.height = height;
    }

    public Button addButton(){
        return Button.builder(name, this::handleButton)
                .bounds(leftPos, topPos, width, height)
                .tooltip(Tooltip.create(name))
                .build();
    }

    private void handleButton(Button button){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player != null) {
            minecraft.player.closeContainer(); // Close GUI

            //Setting the keys to a specific bind and then unbinding allows for all keys to work, even unbound
            tempKey = key.getKey();
            tempSetter = keySetter.getKey();

            changeKey(tempSetter, unbound.getKey(), KeyModifier.NONE);

            //Some keys work with clicks, some work with hold, to make sure all keys activate both are included
            setKey(key, 2, 4);
            clickKeyDelay(key, 1);

        }
    }

    private void changeKey(InputConstants.Key toKey, InputConstants.Key toSetter, KeyModifier mod){
        key.setKeyModifierAndCode(mod, toKey);
        keySetter.setKey(toSetter);
        KeyMapping.resetMapping();
        KeyMapping.resetToggleKeys();
    }

    private void setKey(KeyMapping key, int tickDelay, int tickLength){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                key.setDown(true);
            }
        }, 50L * tickDelay);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                key.setDown(false);
                changeKey(tempKey, tempSetter, modifier);
            }
        }, (50L * tickLength) + (50L * tickDelay)); //timers run at the same time so the delay has to be added to the second timer
    }

    private void clickKeyDelay(KeyMapping key, int tickDelay){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyMapping.click(key.getKey());
            }
        }, 50L * tickDelay);

    }

}
