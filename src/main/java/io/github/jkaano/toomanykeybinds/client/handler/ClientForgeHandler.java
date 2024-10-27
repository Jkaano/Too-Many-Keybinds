package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.screen.TMKPrimaryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {

    private static boolean inKeyChange = false;
    private static boolean oldValue = false;

    private static final Component KEYBIND_MENU = Component.translatable("controls.keybinds.title");
    private static final Component CONTROLS_MENU = Component.translatable("controls.title");

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        Minecraft minecraft = Minecraft.getInstance();
        if(Keybindings.INSTANCE.openTMK.consumeClick() && minecraft.player != null){
            minecraft.setScreen(new TMKPrimaryScreen());
        }
    }

    @SubscribeEvent
    public static void keyChange(ScreenEvent event){
        Component title = event.getScreen().getTitle();
        if(title.equals(KEYBIND_MENU) && !inKeyChange && !oldValue){
            setValue(true); //Says the menu is open, but was not open
        }else if(!title.equals(KEYBIND_MENU) && !title.equals(CONTROLS_MENU) && inKeyChange && !oldValue){

            System.out.println("Too Many Keybinds: Updating keybind list");
            TooManyKeybinds.tmkHandler.update();
            System.out.println("Too Many Keybinds: Done updating keybind list");

            setValue(false); //Says the menu isn't open but was open
        }else if(!inKeyChange && oldValue){
            setValue(false); //Resets both values to false
        }

    }

    private static void setValue(boolean value){
        oldValue = inKeyChange;
        inKeyChange = value;
    }

}
