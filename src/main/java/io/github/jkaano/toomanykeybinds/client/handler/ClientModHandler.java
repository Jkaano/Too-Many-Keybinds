package io.github.jkaano.toomanykeybinds.client.handler;

import io.github.jkaano.toomanykeybinds.TooManyKeybinds;
import io.github.jkaano.toomanykeybinds.client.Keybindings;
import io.github.jkaano.toomanykeybinds.client.KeyHandler;
import io.github.jkaano.toomanykeybinds.client.compatibility.EssentialsCompatibility;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TooManyKeybinds.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModHandler {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){

        //Set compatibilities
        EssentialsCompatibility.setEssentials();

        //Create an initial configuration support for the KeyHandler to use as it defines some variables
        RobotHandler.setCompatConfig(); //Maybe move key press handling to a new class so I don't have to initialize and then reset later?

        System.out.println("Too Many Keybinds: Creating keybind list");
        TooManyKeybinds.tmkHandler = new KeyHandler();
        System.out.println("Too Many Keybinds: Successfully created keybind list");

    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event){
        event.register(Keybindings.INSTANCE.openTMK);
        event.register(Keybindings.INSTANCE.keySetter);
    }

}
