package io.github.jkaano.toomanykeybinds.event;

import io.github.jkaano.toomanykeybinds.Constants;
import io.github.jkaano.toomanykeybinds.data.KeyMappingManager;
import io.github.jkaano.toomanykeybinds.gui.screen.KeySelectScreenForge;
import io.github.jkaano.toomanykeybinds.util.KeyBinding;
import io.github.jkaano.toomanykeybinds.util.KeyboardSimulator;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


/**
 * Register client events for Forge
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            while (KeyBinding.TOO_MANY_KEYBINDS_TOGGLE.consumeClick()) {
                Minecraft.getInstance().setScreen(new KeySelectScreenForge());
                Minecraft.getInstance().player.sendSystemMessage(Component.literal("Toggle Pressed"));
            }

            // Counter for key input
            if (KeyboardSimulator.isCounting) {
                KeyboardSimulator.tickCounter++;
                if (KeyboardSimulator.tickCounter >= KeyboardSimulator.DELAY_TIME) {
                    KeyboardSimulator.getPressedInstance().stopPress();
                }
            }


        }
    }
    // Register KeyMapping
    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.TOO_MANY_KEYBINDS_TOGGLE);
        }

        /*
        Mod setup event. Runs at the end to ensure all modded keybinds are registered.
         */
        @SubscribeEvent
        public static void commonSetup(FMLCommonSetupEvent event) {
            KeyMappingManager keyMappingManager = new KeyMappingManager();
            keyMappingManager.init();
        }
    }

}


