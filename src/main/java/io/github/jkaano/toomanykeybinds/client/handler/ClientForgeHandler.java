package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.config.ClientConfig;
import io.github.jkaano.toomanykeybinds.client.screen.TMKScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    private static boolean update = false;
    private static boolean open = false;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        if(Keybindings.INSTANCE.openTMK.consumeClick() && minecraft.player != null && !open){
            minecraft.setScreen(new TMKScreen());
        }
        if(!ClientConfig.AUTOMATIC_KEY_PRESS.get() && Keybindings.INSTANCE.keySetter.consumeClick() && minecraft.player != null && !open && !TooManyKeybinds.keyQueue.isEmpty()){
            TooManyKeybinds.keyQueue.get(0).removeFromQueue();
        }
    }

//Problem with container opening right after closing
//    @SubscribeEvent
//    public static void guiKeypress(ScreenEvent.KeyPressed event){
//        Minecraft minecraft = Minecraft.getInstance();
//        int key = Keybindings.INSTANCE.openTMK.getKey().getValue();
//        if(Keybindings.INSTANCE.openTMK.isActiveAndMatches(InputConstants.getKey(key, key)) && event.getScreen() instanceof TMKScreen && minecraft.player != null && open){
//            minecraft.player.closeContainer();
//            open = false;
//        }
//    }

    @SubscribeEvent
    public static void guiOpen(ScreenEvent.Opening event){
        if(event.getScreen() instanceof KeyBindsScreen){
            update = true;
        }
        if(event.getScreen() instanceof TMKScreen){
            open = true;
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
        if(event.getScreen() instanceof TMKScreen && open){
            open = false;
        }

    }

}
