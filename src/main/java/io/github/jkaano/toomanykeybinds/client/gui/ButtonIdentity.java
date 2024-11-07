package io.github.jkaano.toomanykeybinds.client.gui;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.compatibility.EssentialsCompatibility;
import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.handler.RobotHandler;
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

    private static final String DISPLAY_MESSAGE = "message." + TooManyKeybinds.MODID + ".display_message";
    private static final String TOO_LATE = "message." + TooManyKeybinds.MODID + ".too_late";

    private final Component name;
    private final KeyMapping key;
    private final KeyModifier modifier;
    private int leftPos, topPos, width, height;

    private final KeyMapping keySetter = Keybindings.INSTANCE.keySetter;

    private InputConstants.Key tempKey;

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
            changeKey(keySetter.getKey(), KeyModifier.NONE);

            //Press the key and release it. Uses a robot to press and then releases both the robot and game input
            if(ClientConfig.AUTOMATIC_KEY_PRESS.get()){
                pressKey(key);
            }else if(!ClientConfig.AUTOMATIC_KEY_PRESS.get()){
                keyDelay(key);
            }else{
                System.out.println("Too Many Keybinds: Hmm... why is the config not true or false, failed to press");
            }


        }
    }

    private void changeKey(InputConstants.Key toKey, KeyModifier mod){
        key.setKeyModifierAndCode(mod, toKey);
        KeyMapping.resetMapping();
    }

    private void pressKey(KeyMapping key){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!EssentialsCompatibility.essentials){
                    RobotHandler.simulateKeyPress(key);
                }else{
                    key.setDown(true);
                    KeyMapping.click(key.getKey());
                }
            }
        }, 50L);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyMapping.set(Keybindings.INSTANCE.keySetter.getKey(), false);
                if(!EssentialsCompatibility.essentials){
                    RobotHandler.simulateKeyRelease(key);
                }else{
                    key.setDown(false);
                }
                changeKey(tempKey, modifier);
            }
        }, (50L * 2) + (50L)); //timers run at the same time so the delay has to be added to the second timer
    }

    private void keyDelay(KeyMapping key){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player != null){
            minecraft.player.displayClientMessage(Component.translatable(DISPLAY_MESSAGE), false);
            minecraft.player.displayClientMessage(Component.translatable(key.getKey().toString()), false);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(minecraft.player != null){
                    minecraft.player.displayClientMessage(Component.translatable(TOO_LATE), false);
                }
                changeKey(tempKey, modifier);
            }
        }, 30000L); //30 second wait

    }

}
