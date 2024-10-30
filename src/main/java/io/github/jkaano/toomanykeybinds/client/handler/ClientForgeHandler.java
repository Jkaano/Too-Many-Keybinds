package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    private static boolean update = false;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        if(Keybindings.INSTANCE.openTMK.consumeClick() && minecraft.player != null){
            minecraft.setScreen(new TMKScreen());
        }
    }

    @SubscribeEvent
    public static void guiOpen(ScreenEvent.Opening event){

        if(event.getScreen() instanceof KeyBindsScreen){
            update = true;
        }
    }

    @SubscribeEvent
    public static void guiClose(ScreenEvent.Closing event){

        if(event.getScreen() instanceof KeyBindsScreen && update){

            System.out.println("Too Many Keybinds: Updating keybind list");
            TooManyKeybinds.tmkHandler.update();
            System.out.println("Too Many Keybinds: Done updating keybind list");

            update = false;

        }

    }

}
