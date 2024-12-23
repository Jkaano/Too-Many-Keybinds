package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.button.ButtonEvent;
import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.screen.MenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        if(event.phase == TickEvent.Phase.END){
            while(Keybindings.INSTANCE.openTMK.consumeClick() && minecraft.player != null){
                if(ButtonEvent.getQueue().isEmpty()){
                    minecraft.setScreen(new MenuScreen());
                }else if(!ClientConfig.AUTOMATIC_KEY_PRESS.get()){
                    ButtonEvent.getQueue().get(0).removeFromQueue();
                }
            }
        }
    }

    @SubscribeEvent
    public static void guiClose(ScreenEvent.Closing event){
        if(event.getScreen() instanceof KeyBindsScreen){
            ClientModHandler.pageHandler.updateBinds();
        }
    }
}
