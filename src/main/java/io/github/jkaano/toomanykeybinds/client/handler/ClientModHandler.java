package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.pages.PageFileHandler;
import io.github.jkaano.toomanykeybinds.client.pages.PageHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {

    public static PageHandler pageHandler;

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){

        //Create an initial configuration support for the KeyHandler to use as it defines some variables
        RobotHandler.setCompatConfig(); //Maybe move key press handling to a new class so I don't have to initialize and then reset later?

        TooManyKeybinds.pageFileHandler = new PageFileHandler();
        pageHandler = new PageHandler();

    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event){
        event.register(Keybindings.INSTANCE.openTMK);
    }

}
