package io.github.jkaano.toomanykeybinds.client.button;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.handler.RobotHandler;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ButtonEvent{
    private static final Component TOO_LATE = Component.translatable(
            "message." + TooManyKeybinds.MODID + ".too_late");

    private final KeyMapping key;

    private final KeyModifier modifier;
    private final InputConstants.Key keyHolder;
    private final InputConstants.Key keySetter;

    private final Timer timer;

    private static boolean usingRobot;
    private static final List<ButtonEvent> queue = new ArrayList<>();

    public boolean inQueue;

    public ButtonEvent(KeyMapping key){
        this.key = key;
        this.keyHolder = key.getKey();
        this.modifier = key.getKeyModifier();

        this.keySetter = Keybindings.INSTANCE.openTMK.getKey();

        usingRobot = RobotHandler.usingRobot();
        timer = new Timer();

        updateBinding(KeyModifier.NONE, keySetter);

    }

    private void updateBinding(KeyModifier modifier, InputConstants.Key toKey){
        key.setKeyModifierAndCode(modifier, toKey);
        KeyMapping.resetMapping();
        KeyMapping.resetToggleKeys();
    }

    public void pressKey(){
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                addToQueue();
                if(usingRobot){RobotHandler.simulateKeyPress(key);}
                else{
                    key.setDown(true);
                    KeyMapping.click(key.getKey());
                }
            }
        }, 50L);

        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                KeyMapping.set(keySetter, false);
                if(usingRobot){RobotHandler.simulateKeyRelease(key);}
                else{
                    key.setDown(false);
                }
                updateBinding(modifier, keyHolder);
                removeFromQueue();
            }
        }, (50L * 2) + 50L);

    }

    public void keyDelay(){
        LocalPlayer player = Minecraft.getInstance().player;
        addToQueue();
        if(player != null){
            Component DISPLAY_MESSAGE = Component.translatable("message." + TooManyKeybinds.MODID + ".display_message", key.getTranslatedKeyMessage().getString(), ClientConfig.PRESS_DELAY.get());
            player.displayClientMessage(DISPLAY_MESSAGE, false);
        }
        timer.schedule(new TimerTask(){
            @Override
            public void run(){
                if(player != null && inQueue){
                    player.displayClientMessage(TOO_LATE, false);
                    removeFromQueue();
                }
            }
        }, ClientConfig.PRESS_DELAY.get() * 1000); //5 Second wait

    }

    private void addToQueue(){
        inQueue = true;
        queue.add(this);
    }

    public void removeFromQueue(){
        inQueue = false;
        queue.remove(this);
        key.setDown(false);
        updateBinding(modifier, keyHolder);
    }

    //Getters
    public static List<ButtonEvent> getQueue(){
        return queue;
    }

}
