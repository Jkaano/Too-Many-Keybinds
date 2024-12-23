package io.github.jkaano.toomanykeybinds.client.button;

import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;

public class ButtonHandler extends ScreenButton{

    private final KeyMapping key;

    public ButtonHandler(KeyMapping key){
        super(key.getName());
        this.key = key;
    }

    @Override
    protected void handleButton(Button button){
        Minecraft mcIn = Minecraft.getInstance();
        if(mcIn.player != null){
            mcIn.player.closeContainer();
        }

        //Press the key and release it.
        //Checks config and decides which way to press the key
        if(ClientConfig.AUTOMATIC_KEY_PRESS.get()){new ButtonEvent(key).pressKey();}
        else{new ButtonEvent(key).keyDelay();}

    }

}
