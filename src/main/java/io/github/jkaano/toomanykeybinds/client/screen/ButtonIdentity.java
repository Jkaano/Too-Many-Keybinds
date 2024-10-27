package io.github.jkaano.toomanykeybinds.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class gives each button a unique identity based on the which Key identity it was created with.
 * It also handles the buttons actions, by moving it into its own class each button is its own object with handler.
 */
public class ButtonIdentity{

    private final Component name;
    private final KeyMapping key;
    private final int leftPos, topPos, width, height;

    private final KeyMapping keySetter = Keybindings.INSTANCE.keySetter;
    private final KeyMapping unbound = Keybindings.INSTANCE.unbound;

    private final Timer timer = new Timer();

    public ButtonIdentity(KeyMapping key, int leftPos, int topPos, int width, int height){
        this.key = key;
        this.name = Component.translatable(key.getName());
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
            InputConstants.Key tempKey = key.getKey();
            InputConstants.Key tempSetter = keySetter.getKey();

            key.setKey(keySetter.getKey());
            keySetter.setKey(unbound.getDefaultKey());
            KeyMapping.resetMapping();

            //Some keys work with clicks, some work with hold, to make sure all keys activate both are included
            clickKeyDelay(key, 1);
            setKey(key, 2, 10);

            key.setKey(tempKey);
            keySetter.setKey(tempSetter);
            KeyMapping.resetMapping();

        }
    }

    private void setKey(KeyMapping key, int tickDelay, int tickLength){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyMapping.set(key.getKey(), true);
            }
        }, 50L * tickDelay);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyMapping.set(key.getKey(), false);
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
